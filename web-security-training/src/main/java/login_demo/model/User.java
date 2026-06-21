package login_demo.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class User {

    public enum UserRole {
        USER,
        ADMIN
    }

    public enum UserState {
        ACTIVE,
        DISABLED,
        LOCKED
    }

    private long id;
    private String login;
    private String hashPassword;
    private Boolean enabled;

    private UserRole role;
    private UserState state;

    private LocalDateTime registeringTime;
    private LocalDateTime lastUpdateTime;
}
