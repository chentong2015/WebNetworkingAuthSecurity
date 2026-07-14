package org.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CorsController2 {

    // TODO. 后端配置CORS响应头，允许特定源访问
    @GetMapping("/value")
    public String getValue(HttpServletRequest request, HttpServletResponse response) {
          response.addHeader("Access-Control-Allow-Origin", "*");
          response.addHeader("Access-Control-Allow-Methods", "GET");
          return "get value";
    }
}