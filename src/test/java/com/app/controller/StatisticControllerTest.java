package com.app.controller;

import com.app.component.DataStorageComponent;
import com.app.model.Statistic;
import com.app.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Arrays;
import org.apache.http.HttpStatus;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class StatisticControllerTest extends BaseControllerTest {

    @Autowired
    DataStorageComponent dataStore;

    @Before
    public void setUp() {
        dataStore.resetBuckets(); // reset it...
        
        RestAssured.port = port;
        Arrays.asList(
                new Transaction(50, getSecondsAgo(50)), 
                new Transaction(40, getSecondsAgo(40)),
                new Transaction(30, getSecondsAgo(30)))
                .stream()
                .forEach(t -> dataStore.addTransaction(t));
    }

    private Response getStatisticsService() {
        return given().contentType("application/json")
                .when().get("/statistics");
    }
    
    @Test
    public void testReturnOk() {
        Response resp = getStatisticsService();
        assertEquals(resp.getStatusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void testResponse() throws JsonProcessingException, IOException {
        Response resp = getStatisticsService();
        Statistic result = new ObjectMapper().readValue(resp.getBody().asString(), Statistic.class);
        assertEquals(result.getSum(), 120, 0);
        assertEquals(result.getAvg(), 40, 0);
        assertEquals(result.getMax(), 50, 0);
        assertEquals(result.getMin(), 30, 0);
        assertEquals(result.getCount(), 3, 0);
    }
}
