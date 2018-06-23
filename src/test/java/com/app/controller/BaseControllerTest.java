package com.app.controller;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
public class BaseControllerTest {    

    @Value("${local.server.port}")
    protected int port;
    
    protected long getSecondsAgo(long time) {
        return Instant.now().minusSeconds(time).toEpochMilli();
    }
}
