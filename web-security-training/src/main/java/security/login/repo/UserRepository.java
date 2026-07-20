package security.login.repo;

import security.login.model.User;
import security.login.model.UserAuthEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserAuthEntity> findAuthDataByLogin(String login);

    // 注册一个新的用户
    User save(User user);
}