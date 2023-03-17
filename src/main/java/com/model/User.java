package com.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("User")
public class User  {
        @Id
        private UUID id;
        @NotNull
        @Size(min = 4, max = 24)
        private String username;
        private String firstName;
        private String lastName;
        @Getter(onMethod = @__(@JsonIgnore))
        @NotNull
        private String password;
        private String nickname;
        private String mobile;
        @Email
        @NotNull
        private String email;
        private boolean enabled;
        private List<GroupRole> roles;
        @NotNull
        private UUID customerAccountId;

    }
