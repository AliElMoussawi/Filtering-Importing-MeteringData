package com.dto.killbill;

import lombok.Data;
import org.joda.time.LocalDate;

import java.util.UUID;
@Data
public class UsageRecordRequestDto {
    UUID subscriptionId;
    LocalDate startDate;
    LocalDate endDate;
}
