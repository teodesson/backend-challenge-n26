package com.app.helper;

import com.app.model.Statistic;
import com.app.model.Transaction;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
public class TransactionBucket extends Statistic {

    private long timestamp;

    public TransactionBucket() {        
    }
    
    public TransactionBucket(final Transaction transaction) {
        assignTransaction(transaction);
    }
    
    public TransactionBucket(final Transaction transaction, final TransactionBucket otherBucket) {
        assignTransaction(transaction);
        mergeStatistic(otherBucket);
    }

    private void assignTransaction(final Transaction transaction) {
        // update to the latest timestamp
        if (transaction.getTimestamp() > this.timestamp) {
            this.timestamp = transaction.getTimestamp();
        }
        this.setSum(transaction.getAmount());
        this.setCount(1);
        this.setAvg(transaction.getAmount());
        this.setMax(transaction.getAmount());
        this.setMin(transaction.getAmount());
    }
    
    /**
     * Get latest timestamp from transaction inserted in the bucket
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
}
