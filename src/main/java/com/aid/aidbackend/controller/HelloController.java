package com.aid.aidbackend.controller;

import com.aid.aidbackend.rest.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping(path = "/hello")
    public ApiResult<Map<String, String>> hello() {
        return ApiResult.succeed(
                new HashMap<>() {{
                    put("message", "hello");
                }}
        );
    }

}
