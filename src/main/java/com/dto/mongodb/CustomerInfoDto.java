package com.dto.mongodb;

import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
public class CustomerInfoDto {
    String userEmail;
    UUID customerAccountId;
}