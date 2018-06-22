package com.app.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@Data
@AllArgsConstructor
public class Statistic implements Serializable {

    private static final long serialVersionUID = -91650430L;

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;
}
