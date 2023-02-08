package com.example.client.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class WebClientConfig {

    @Value("${client.ssl.trust-store}")
    private String trustStoreLocation;

    @Value("${client.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${client.ssl.key-store}")
    private String keyStoreLocation;

    @Value("${client.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${client.ssl.key-password}")
    private String keyPassword;

    @Bean
    public WebClient webClient(ClientHttpConnector connector) throws Exception {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

        KeyStore keyStore1 = KeyStore.getInstance("JKS");
        keyStore1.load(new FileInputStream(ResourceUtils.getFile(keyStoreLocation)), keyStorePassword.toCharArray());

        keyManagerFactory.init(keyStore1, keyPassword.toCharArray());

        KeyStore trustStore1 = KeyStore.getInstance("JKS");
        trustStore1.load(new FileInputStream(ResourceUtils.getFile(trustStoreLocation)), trustStorePassword.toCharArray());

        trustManagerFactory.init(trustStore1);

        SslContext sslContext = SslContextBuilder.forClient()
                .keyManager(keyManagerFactory)
                .trustManager(trustManagerFactory)
                .build();

        HttpClient httpClient = HttpClient.create().secure(spec -> spec.sslContext(sslContext));

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
