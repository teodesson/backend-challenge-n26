package com.app.controller;

import com.app.component.DataStorageComponent;
import com.app.model.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@RestController
public class StatisticController {
    
    @Autowired
    DataStorageComponent dataStore;
    
    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Statistic> getStatistics(){
            return ResponseEntity.ok(dataStore.getStatistics());
    }
}
