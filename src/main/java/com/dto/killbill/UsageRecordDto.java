package com.dto.killbill;

import lombok.Data;
import org.joda.time.LocalDate;

import java.util.UUID;

@Data
public class UsageRecordDto {

    Long amount;
    LocalDate startDate;
    LocalDate endDate;

    public UsageRecordDto( Long amount, LocalDate startDate, LocalDate endDate)

    {this.amount=amount;
    this.startDate=startDate;
    this.endDate=endDate;}
}
