package com.example.server.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class DummyController {

    @GetMapping("/test")
    public String dummyServerController() {
        System.out.println("XXX::" + SecurityContextHolder.getContext().getAuthentication().getName());
        return "Returning from server";
    }
}
