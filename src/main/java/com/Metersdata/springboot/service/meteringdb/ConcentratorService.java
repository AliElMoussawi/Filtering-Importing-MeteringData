package com.Metersdata.springboot.service.meteringdb;

import com.Metersdata.springboot.repository.ConcentratorRepository;
import com.Metersdata.springboot.model.Concentrator;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class ConcentratorService {

    @Autowired
    ConcentratorRepository concentratorRepository;

    public Concentrator createConcentrator(){
        Concentrator concentrator=new Concentrator(UUID.randomUUID() ,"Spain", Math.abs(new Random().nextLong())  ,UUID.randomUUID() ,new DateTime() );
        return concentratorRepository.insert(concentrator);
    }
}
