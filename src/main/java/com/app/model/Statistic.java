package com.app.model;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@Data
@NoArgsConstructor
public class Statistic implements Serializable {

    private static final long serialVersionUID = -19800409L;

    private double sum;
    private double avg;
    private double max;
    private double min;
    private long count;

    public void mergeStatistic(final Statistic otherStatistic) {
        if (otherStatistic.count>0) {
            if (this.count>0) {
                this.min = Math.min(this.min, otherStatistic.min);
            }
            else {
                this.min = otherStatistic.min;
            }
            this.avg = ((otherStatistic.avg * otherStatistic.count) + (this.avg * this.count)) / (otherStatistic.count + this.count);
            this.sum = otherStatistic.sum + this.sum;
            this.count = otherStatistic.count + this.count;
            this.max = Math.max(this.max, otherStatistic.max);
        }
    }
}
