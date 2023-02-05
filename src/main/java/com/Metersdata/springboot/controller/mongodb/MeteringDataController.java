package com.Metersdata.springboot.controller.mongodb;

import com.Metersdata.springboot.model.MeteringData;
import com.Metersdata.springboot.repository.MeteringDataRepository;
import com.Metersdata.springboot.service.meteringdb.MeteringDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/meteringData")
@CrossOrigin
public class MeteringDataController {
    @Autowired
    MeteringDataService meteringDataService;
    @Autowired
    MeteringDataRepository meteringDataRepository;
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<List<MeteringData>> getMeteringData() throws Exception {
        return new ResponseEntity<>(meteringDataRepository.findAll(), HttpStatus.OK);
    }


    }