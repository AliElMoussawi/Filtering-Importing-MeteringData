package com.configuration;

import org.killbill.billing.client.KillBillHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KillBillConfiguration {
    @Value("${killbill.url}")
    private String killBillUrl;
    @Value("${killbill.api.Username}")
    private String userName;
    @Value("${killbill.api.Password}")
    private String password;
    @Value("${killbill.api.key}")
    private String apiKey;
    @Value("${killbill.api.secret}")
    private String apiSecret;

    @Value("${killbill.Default_connection_timeout_sec}")
    private int defaultConnectionTimeoutSec;
    @Value("${killbill.Default_read_timeout_sec}")
    private int defaultReadTimeoutSec;
    @Value("${killbill.Default_request_timeout_sec}")
    private int defaultRequestTimeoutSec;

    @Bean
    public KillBillHttpClient killBillClient() {
        return new KillBillHttpClient(killBillUrl, userName, password, apiKey,apiSecret, null, null, defaultConnectionTimeoutSec * 1000, defaultReadTimeoutSec * 1000,defaultRequestTimeoutSec  * 1000);

    }

}