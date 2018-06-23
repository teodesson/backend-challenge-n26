package com.app.component;

import com.app.config.AppConfig;
import com.app.helper.TransactionBucket;
import com.app.model.Statistic;
import com.app.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@Component
public class DataStorageComponent {

    private final TransactionBucket[] transactionBuckets;
    
    @Autowired
    public DataStorageComponent() {
        transactionBuckets = new TransactionBucket[AppConfig.MS_2_RECYCLE_A_BUCKET / AppConfig.MS_2_RETAIN_IN_BUCKET];
        for (int i = 0; i < transactionBuckets.length; i++) {
            transactionBuckets[i] = new TransactionBucket();
        }
    }

    private int getTransactionBucketsIndexForTimestamp(final long timestamp) {
        return (int) ((timestamp / AppConfig.MS_2_RETAIN_IN_BUCKET) % transactionBuckets.length);
    }

    private long getTimestampCutOff() {
        return System.currentTimeMillis() - AppConfig.MS_2_RECYCLE_A_BUCKET;
    }

    public boolean isValidTransactionTimestamp(final long timestamp) {
        return timestamp >= getTimestampCutOff();
    }

    public boolean addTransaction(final Transaction transaction) {
        final int index = getTransactionBucketsIndexForTimestamp(transaction.getTimestamp());
        if (isValidTransactionTimestamp(transaction.getTimestamp())) {
            synchronized (transactionBuckets[index]) {
                transactionBuckets[index] = new TransactionBucket(transaction, transactionBuckets[index]);
            }
            return true;
        } else {
            synchronized (transactionBuckets[index]) {
                // reset
                transactionBuckets[index] = new TransactionBucket();
            }
            return false;
        }
    }

    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        for (int i = 0; i < transactionBuckets.length; i++) {
            if (isValidTransactionTimestamp(transactionBuckets[i].getTimestamp())) {
                statistic.mergeStatistic(transactionBuckets[i]);
            }
        }
        return statistic;
    }

}
