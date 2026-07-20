package security.login.service;

import security.login.jwt.JwtTokenHelper;
import security.login.jwt.TokenDto;
import security.login.form.SignUpForm;
import security.login.model.User;
import security.login.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

// @Service
public class SignupServerImpl {

    private CaptchaService captchaService;

    private PasswordEncoder passwordEncoder;
    private JwtTokenHelper jwtTokenHelper;

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    public TokenDto signUp(SignUpForm form) {
        captchaService.processResponse(form.getCaptchaResponse());

        String passwordHash = passwordEncoder.encode(form.getPassword());
        User userCandidate = userRepository.save(User.builder()
                .login(form.getLogin())
                .hashPassword(passwordHash)
                .enabled(Boolean.TRUE)
                .role(User.UserRole.USER)
                .state(User.UserState.ACTIVE)
                .registeringTime(LocalDateTime.now())
                .lastUpdateTime(LocalDateTime.now())
                .build());

        UserDetails userDetails = userDetailsService.loadUserByUsername(form.getLogin());
        return TokenDto.builder()
                .token(jwtTokenHelper.generateToken(userDetails))
                .build();
    }
}
