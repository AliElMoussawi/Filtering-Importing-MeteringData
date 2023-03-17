package com.dto.shiro;

import lombok.Data;

@Data
public class LoginRequest {
    private String Username;
    private String password;
}
