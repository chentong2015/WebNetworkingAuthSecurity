package org.example;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @PostMapping("/data/{id}")
    public String getData(@PathVariable(value = "id") String id) {
        System.out.println("call data for " + id);
        return "data value";
    }
}
