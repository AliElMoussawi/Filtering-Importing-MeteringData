package com.controller.testing;

import com.dto.mongodb.requestDto;
import com.model.MeteringData;
import com.repository.MeteringDataRepository;
import com.service.killbill.BundleService;
import com.service.meteringdb.MeteringDataService;
import com.util.data.EnergyConsumptionData;
import com.util.generator.EnergyConsumptionGenerator;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/testing")
@CrossOrigin
public class testingController {
    @Autowired
    MeteringDataService  meteringDataService;
    @Autowired
    MeteringDataRepository meteringDataRepository;
    @Autowired
    BundleService bundleService;
    @RequestMapping(method = RequestMethod.POST, value = "/t")
    public ResponseEntity<EnergyConsumptionData> createEnergyConcumption(@RequestBody @NotNull requestDto id) throws Exception {
        EnergyConsumptionData energyConsumptionData= new EnergyConsumptionGenerator().generateData(id.getId());
        System.out.println(id);
        return (energyConsumptionData ==null)? null: new ResponseEntity<>(energyConsumptionData, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/t")
    public ResponseEntity<List<MeteringData>> getEnergyConcumption(@RequestBody @NotNull requestDto id) throws Exception {
        List<MeteringData> meteringDataList =  meteringDataRepository.findBySmartMeterIdAndRetrievedIsFalse(id.getId());
        return new ResponseEntity<>(meteringDataList, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/meterId_KillBill")
    public ResponseEntity<Map<String ,UUID>> getMeterIdFromKillBill() throws Exception {
        Map<String ,UUID> subcriptionsMeterId=bundleService.getSmartMeterAndSubscriptionIds();
        return new ResponseEntity<>(subcriptionsMeterId, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/unretrievedmeteringData")
    public ResponseEntity<List<MeteringData>> getMeterId() throws Exception {
        Map<String ,UUID> subscriptionsMeterId=bundleService.getSmartMeterAndSubscriptionIds();
        List<String> smartMeterIds = subscriptionsMeterId.values().stream()
                .map(UUID::toString)
                .collect(Collectors.toList());
        List<MeteringData> meteringData = meteringDataService.returnMeteringData(smartMeterIds) ;
        return new ResponseEntity<>(meteringData, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/findAll")
    public ResponseEntity<List<String>> findAllSmartMeterIds() throws Exception {
        List<String>meteringData=meteringDataService.findAllSmartMeterIds();
        return new ResponseEntity<>(meteringData, HttpStatus.OK);
    }
}
