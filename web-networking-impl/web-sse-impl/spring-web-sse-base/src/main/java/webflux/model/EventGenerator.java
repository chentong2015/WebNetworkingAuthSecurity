package webflux.model;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class EventGenerator {

    private final List<String> events = Arrays.asList("Bonjour", "Hola", "Zdravstvuyte", "Guten Tag", "Hello");
    private final Random random = new Random(events.size());

    // 随机生成事件列表
    public String generate() {
        return events.get(random.nextInt(events.size()));
    }
}
