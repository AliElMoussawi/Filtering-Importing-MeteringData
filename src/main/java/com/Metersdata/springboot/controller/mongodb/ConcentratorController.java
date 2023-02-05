package com.Metersdata.springboot.controller.mongodb;

import com.Metersdata.springboot.model.Concentrator;
import com.Metersdata.springboot.service.meteringdb.ConcentratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/concentrator")
@CrossOrigin
public class ConcentratorController {
    @Autowired
    ConcentratorService concentratorService;

    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<Concentrator> createConcentrator() throws Exception {
        Concentrator concentrator=concentratorService.createConcentrator();
        return (concentrator==null)? null: new ResponseEntity<>(concentrator, HttpStatus.OK);
    }


    }
