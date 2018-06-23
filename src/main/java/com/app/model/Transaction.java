package com.app.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@Data
@AllArgsConstructor
public class Transaction implements Serializable {

    private static final long serialVersionUID = -91650430L;
    
    private double amount;
    private long timestamp;    
}
