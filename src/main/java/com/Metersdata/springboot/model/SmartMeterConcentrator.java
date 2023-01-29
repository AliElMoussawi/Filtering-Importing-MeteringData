package com.Metersdata.springboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Concentrator concentrator;
    @OneToOne
    private SmartMeter smartMeter;

}