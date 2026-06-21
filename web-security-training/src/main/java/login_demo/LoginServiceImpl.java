package login_demo;

import jakarta.servlet.http.HttpServletRequest;
import login_demo.form.SignInForm;
import login_demo.jwt.JwtTokenHelper;
import login_demo.model.LoginAttemptCounter;
import login_demo.model.User;
import login_demo.model.UserAuthEntity;
import login_demo.jwt.TokenDto;
import login_demo.repo.LoginAttemptCounterRepository;
import login_demo.repo.UserRepository;
import login_demo.service.CaptchaService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

// 关于登录限制的相关逻辑: 限制IP地址的多次访问
// @Service
public class LoginServiceImpl {

    private CaptchaService captchaService;

    private PasswordEncoder passwordEncoder;
    private JwtTokenHelper jwtTokenHelper;

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private LoginAttemptCounterRepository loginAttemptRepository;

    private static final int MAX_FAILED_ATTEMPT_COUNT = 5;
    private static final int BLOCKED_HOURS_COUNT = 3;

    // TODO. 登录逻辑必须验证Password是否正确, 不能只判断数据表中用户ID的存在 !!
    public TokenDto signIn(SignInForm form, HttpServletRequest request) {
        captchaService.processResponse(form.getCaptchaResponse());
        checkClientAddressBeforeLogin(request);

        Optional<UserAuthEntity> authCandidate = userRepository.findAuthDataByLogin(form.getLogin());
        authCandidate.ifPresent(userAuthEntity -> checkUserBeforeLogin(userAuthEntity.getId()));
        if (authCandidate.isEmpty()) {
            processFailedLoginAttemptByClientAddress(request);
            throw new RuntimeException();
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(form.getLogin());
        return TokenDto.builder()
                .token(jwtTokenHelper.generateToken(userDetails))
                .build();
    }

    // TODO. 基于IP地址来判断请求是否被Blocked
    private void checkClientAddressBeforeLogin(HttpServletRequest request) {
        String clientAddress = getClientIP(request);
        Optional<LoginAttemptCounter> attemptCandidate = loginAttemptRepository.findByClientAddress(clientAddress);
        if (attemptCandidate.isPresent()) {
            LoginAttemptCounter loginAttempt = attemptCandidate.get();
            if (loginAttempt.getBlockedEndTime().isAfter(LocalDateTime.now())) {
                throw new RuntimeException();
            }
        }
    }

    // TODO. 基于用户ID来判断用户是否被Blocked
    private void checkUserBeforeLogin(Long userId) {
        User user = User.builder().id(userId).build();
        Optional<LoginAttemptCounter> attemptCandidate = loginAttemptRepository.findByUser(user);
        if (attemptCandidate.isPresent()) {
            LoginAttemptCounter loginAttempt = attemptCandidate.get();
            if (loginAttempt.getBlockedEndTime().isAfter(LocalDateTime.now())) {
                throw new RuntimeException();
            }
        }
    }

    private void processFailedLoginAttemptByClientAddress(HttpServletRequest request) {
        String clientAddress = getClientIP(request);
        Optional<LoginAttemptCounter> attemptCandidate = loginAttemptRepository.findByClientAddress(clientAddress);
        if (attemptCandidate.isPresent()) {
            processExistedAttempt(attemptCandidate.get());
            return;
        }

        LoginAttemptCounter newAttempt = LoginAttemptCounter.builder()
                .lastUpdateTime(LocalDateTime.now())
                .count(1)
                .clientAddress(clientAddress)
                .build();
        loginAttemptRepository.save(newAttempt);
    }

    private void processFailedLoginAttemptByUser(Long userId) {
        User user = User.builder().id(userId).build();
        Optional<LoginAttemptCounter> attemptCandidate = loginAttemptRepository.findByUserId(userId);
        if (attemptCandidate.isPresent()) {
            processExistedAttempt(attemptCandidate.get());
            return;
        }

        LoginAttemptCounter newAttempt = LoginAttemptCounter.builder()
                .count(1)
                .user(user)
                .lastUpdateTime(LocalDateTime.now())
                .build();
        loginAttemptRepository.save(newAttempt);
    }

    // 更新操作时间并增加失败的尝试次数
    private void processExistedAttempt(LoginAttemptCounter loginAttempt) {
        loginAttempt.setLastUpdateTime(LocalDateTime.now());
        loginAttempt.setCount(loginAttempt.getCount() + 1);

        if (loginAttempt.getCount() > MAX_FAILED_ATTEMPT_COUNT) {
            loginAttempt.setBlockedEndTime(LocalDateTime.now().plusHours(BLOCKED_HOURS_COUNT));
            throw new RuntimeException();
        }
    }

    private String getClientIP(HttpServletRequest request) {
        final String xfHeader = request.getHeader("CLIENT_IP_HEADER");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}