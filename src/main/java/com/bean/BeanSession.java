package com.bean;

import com.model.User;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author CLICK ONCE
 */

@Component
@Data
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BeanSession {
    private Integer id;
    private String token;
    private User user;




}