package com.example.client.config;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Configuration
public class RestTemplateConfig {

    @Value("${client.ssl.trust-store}")
    private String trustStore;

    @Value("${client.ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${client.ssl.key-store}")
    private String keyStore;

    @Value("${client.ssl.key-store-password}")
    private String keyStorePassword;

    @Value("${client.ssl.key-password}")
    private String keyPassword;

    @Bean
    public RestTemplate restTemplate() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadKeyMaterial(ResourceUtils.getFile(keyStore), keyStorePassword.toCharArray(), keyPassword.toCharArray())
                .loadTrustMaterial(ResourceUtils.getFile(trustStore), trustStorePassword.toCharArray())
                .build();

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();

        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);

        return new RestTemplate(clientHttpRequestFactory);
    }
}
