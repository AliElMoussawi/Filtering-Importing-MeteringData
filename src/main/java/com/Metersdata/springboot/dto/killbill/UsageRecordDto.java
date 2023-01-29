package com.Metersdata.springboot.dto.killbill;

import org.joda.time.LocalDate;

import java.util.UUID;

public class UsageRecordDto {

    UUID accountId;
    LocalDate startDate;
    LocalDate endDate;

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
