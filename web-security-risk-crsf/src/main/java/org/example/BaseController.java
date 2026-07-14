package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

    @GetMapping("/data")
    public String getData() {
        System.out.println("call data");
        return "data value";
    }
}
