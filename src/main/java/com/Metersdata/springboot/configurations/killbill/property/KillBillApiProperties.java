package com.Metersdata.springboot.configurations.killbill.property;

import org.killbill.billing.client.RequestOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KillBillApiProperties {

    @Value("${killbill.api.key}")
    private String apiKey;
    @Value("${killbill.api.secret}")
    private String apiSecret;
    @Value("${killbill.createBy}")
    private String createdBy ;

    private RequestOptions requestOptions ;


    public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getApiSecret() {
            return apiSecret;
        }

        public void setApiSecret(String apiSecret) {
            this.apiSecret = apiSecret;
        }

    public RequestOptions getRequestOptions() {
        requestOptions = RequestOptions.builder().withCreatedBy(createdBy).withTenantApiKey(apiKey).withTenantApiSecret(apiSecret).build();
        return requestOptions;
    }

}