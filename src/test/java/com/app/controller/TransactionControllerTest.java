package com.app.controller;

import com.app.model.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionControllerTest extends BaseControllerTest {

    private String transaction;

    @Before
    public void setUp() {        
        RestAssured.port = port;
    }

    @Test
    public void testCreated() {
        try {
            transaction = new ObjectMapper().writeValueAsString(new Transaction(10D, getSecondsAgo(30)));
        } catch (JsonProcessingException ex) {
            Assert.fail(ex.getMessage());
        }
        
        Response resp = given().contentType("application/json")
                .body(transaction).when().post("/transactions");

        Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }
    
    @Test
    public void testNoContent() {
        try {
            transaction = new ObjectMapper().writeValueAsString(new Transaction(10D, getSecondsAgo(70)));
        } catch (JsonProcessingException ex) {
            Assert.fail(ex.getMessage());
        }
        
        Response resp = given().contentType("application/json")
                .body(transaction).when().post("/transactions");

        Assertions.assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }
}
