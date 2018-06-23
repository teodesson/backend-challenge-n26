package com.app.component;

import com.app.config.AppConfig;
import com.app.helper.TransactionBucket;
import com.app.model.Statistic;
import com.app.model.Transaction;
import com.app.util.TimeUtil;
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
        resetBuckets();        
    }

    public void resetBuckets() {
        for (int i = 0; i < transactionBuckets.length; i++) {
            transactionBuckets[i] = new TransactionBucket();
        }
    }
    
    private int getTransactionBucketsIndex(final long timestamp) {
        return (int) ((timestamp / AppConfig.MS_2_RETAIN_IN_BUCKET) % transactionBuckets.length);
    }

    public boolean isValidTransactionTimestamp(final long timestamp, long timestampCutOff) {
        return timestamp >= timestampCutOff;
    }

    public boolean addTransaction(final Transaction transaction) {
        if (isValidTransactionTimestamp(transaction.getTimestamp(), TimeUtil.getTimestampCutOff())) {
            final int index = getTransactionBucketsIndex(transaction.getTimestamp());
            synchronized (transactionBuckets[index]) {
                if (transactionBuckets[index].getTimestamp() + AppConfig.MS_2_RECYCLE_A_BUCKET < transaction.getTimestamp()) {
                    // RESET bucket!!!
                    transactionBuckets[index] = new TransactionBucket(transaction);                    
                }
                else {
                    transactionBuckets[index] = new TransactionBucket(transaction, transactionBuckets[index]);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Statistic getStatistics() {
        Statistic statistic = new Statistic();
        long timestampCutOff = TimeUtil.getTimestampCutOff(); // timestamp cutoff when statistics called
        for (int i = 0; i < transactionBuckets.length; i++) {
            if (isValidTransactionTimestamp(transactionBuckets[i].getTimestamp(), timestampCutOff)) {
                statistic.mergeStatistic(transactionBuckets[i]);
            }
        }
        return statistic;
    }
}
