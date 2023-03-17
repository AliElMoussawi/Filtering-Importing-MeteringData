package com.dto.killbill;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.UUID;

@Data
public class AnalyticsRequestDto {
    String groupedBy;
   DateTime startDate;
    DateTime endDate;
}
