package com.example.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final RestTemplate restTemplate;
    private final WebClient webClient;

    public ClientController(RestTemplate restTemplate, WebClient webClient) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
    }

    @GetMapping("/test/1")
    public String dummyClientRestTemplateController() {
        System.out.println("XXX::" + SecurityContextHolder.getContext().getAuthentication().getName());
        ResponseEntity<String> response = restTemplate
                .getForEntity("https://localhost:8090/server/test", String.class);
        return "RestTemplate -> Returning from client -> " + response.getBody();
    }

    @GetMapping("/test/2")
    public String dummyClientWebClientController() {
        String response = webClient.get().uri("https://localhost:8090/server/test")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return "WebClient -> Returning from client -> " + response;
    }

}
