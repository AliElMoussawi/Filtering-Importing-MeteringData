package com.dto.shiro;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String idToken;
    private UUID customerAccountId;
}
