package login_demo.repo;

import login_demo.model.LoginAttemptCounter;
import login_demo.model.User;

import java.util.Optional;

// TODO. 数据库中维护一张"用户登录失败次数"的表
public interface LoginAttemptCounterRepository {

    // 基于IP地址的限制
    Optional<LoginAttemptCounter> findByClientAddress(String clientAddress);

    // 基于用户ID的限制
    Optional<LoginAttemptCounter> findByUser(User user);

    Optional<LoginAttemptCounter> findByUserId(Long userId);

    // 存储首次尝试失败记录(count = 1)
    void save(LoginAttemptCounter loginAttemptCounter);
}
