package webflux.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

// Event as domain transfer object
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private String nickname;
    private String text;
    private Instant timestamp;
}
