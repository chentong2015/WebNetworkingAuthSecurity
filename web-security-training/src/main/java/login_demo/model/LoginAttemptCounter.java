package login_demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LoginAttemptCounter {

    private Integer count;

    private User user;
    private String clientAddress;

    private LocalDateTime lastUpdateTime;
    private LocalDateTime blockedEndTime;
}
