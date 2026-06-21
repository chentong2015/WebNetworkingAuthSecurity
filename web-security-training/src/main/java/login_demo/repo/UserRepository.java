package login_demo.repo;

import login_demo.model.User;
import login_demo.model.UserAuthEntity;

import java.util.Optional;

public interface UserRepository {

    Optional<UserAuthEntity> findAuthDataByLogin(String login);

    // 注册一个新的用户
    User save(User user);
}