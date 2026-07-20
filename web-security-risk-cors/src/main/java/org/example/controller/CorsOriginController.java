package org.example.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsOriginController {

    // TODO. 允许特定源的JS代码访问此API
    @GetMapping("/data")
    @CrossOrigin(origins = "http://localhost:63342")
    public String getData() {
        System.out.println("Call getData function");
        return "get data";
    }
}