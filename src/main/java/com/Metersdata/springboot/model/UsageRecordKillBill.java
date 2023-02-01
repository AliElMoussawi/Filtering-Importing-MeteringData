package com.Metersdata.springboot.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.OneToMany;
import java.util.UUID;


@Data
@AllArgsConstructor
@Document("UsageRecord_KillBill")
public class UsageRecordKillBill {
    @Id
    private UUID id;

    @Unique
    @Nullable
    private SmartMeter smartMeter;

}