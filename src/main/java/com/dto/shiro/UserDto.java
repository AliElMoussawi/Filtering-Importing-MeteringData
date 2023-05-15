package com.dto.shiro;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;


@Data
public class UserDto {
    @Id
    private UUID id;

    @NotNull
    @Size(min = 4, max = 24)
    @Unique
    @NotBlank(message = "username is mandatory")
    private String username;
    @JsonProperty("firstname")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;

    @NotNull
    private String password;

    @Email
    @Unique
    @NotNull
    @NotBlank(message = "email is mandatory")
    private String email;

    private boolean enabled;

    @JsonProperty("roles")
    private List<GroupRoleDto> groupRoles;

    private String nickname;
    @Unique
    private String mobile;
    @JsonProperty("customerAccountId")
    private String customerAccountId;


}