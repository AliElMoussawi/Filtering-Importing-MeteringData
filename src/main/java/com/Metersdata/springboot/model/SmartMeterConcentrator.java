package com.Metersdata.springboot.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Unwrapped;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.UUID;


@Data
@AllArgsConstructor
@Document("SmartMeter-DCU")
public class SmartMeterConcentrator {
    @Id
    private UUID id;

    @OneToMany
    @Nullable
    private UUID concentratorId;

    @Unique
    @Nullable
    private UUID smartMeterId;

}