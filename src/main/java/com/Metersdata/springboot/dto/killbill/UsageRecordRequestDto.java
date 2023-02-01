package com.Metersdata.springboot.dto.killbill;

import lombok.Data;
import org.joda.time.LocalDate;

import java.util.UUID;
@Data
public class UsageRecordRequestDto {

    UUID accountId;
    LocalDate startDate;
    LocalDate endDate;
}
