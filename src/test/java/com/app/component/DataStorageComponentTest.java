package com.app.component;


import com.app.model.Statistic;
import com.app.model.Transaction;
import com.app.util.TimeUtil;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class DataStorageComponentTest {

    @Autowired
    DataStorageComponent dataStore;

    private static final int CNT_TRANSACTION_4_STATISTICS = 10000;
    private static final double DELTA = 0.000001;
    
    @Before
    public void setUp() {
       dataStore.resetBuckets();
    }
    
    @Test
    public void testEmptyStatistic() {
        final Statistic statistic = dataStore.getStatistics();
        assertTrue(statistic.getCount() == 0);
    }

    @Test
    public void testValidTransactionPosted() {
        assertTrue(dataStore.addTransaction(new Transaction(Math.random(), TimeUtil.getTimestampCutOff()+100)));
    }
    
    @Test
    public void testExpiredTransactionPosted() {
        assertFalse(dataStore.addTransaction(new Transaction(Math.random(), TimeUtil.getTimestampCutOff()-100)));
    }

    @Test
    public void testValidTransactionPostedThenCheckStatistic() {
        final Transaction transaction = new Transaction(Math.random(), TimeUtil.getTimestampCutOff()+10000);
        assertTrue(dataStore.addTransaction(transaction));
        
        final Statistic statistic = dataStore.getStatistics();
        assertEquals(statistic.getCount(), 1L);
        assertEquals(statistic.getSum(), transaction.getAmount(), 0);
        assertEquals(statistic.getAvg(), transaction.getAmount(), 0);
        assertEquals(statistic.getMin(), transaction.getAmount(), 0);
        assertEquals(statistic.getMax(), transaction.getAmount(), 0);        
    }
    
    @Test
    public void testValidTransactionPostedThenExpiredCheckStatistic() throws InterruptedException {
        final Transaction transaction = new Transaction(Math.random(), TimeUtil.getTimestampCutOff() + 3000);
        assertTrue(dataStore.addTransaction(transaction));
        
        Statistic statistic = dataStore.getStatistics();
        assertEquals(statistic.getCount(), 1);
        assertEquals(statistic.getAvg(), transaction.getAmount(), 0);
        assertEquals(statistic.getMax(), transaction.getAmount(), 0);
        
        Thread.sleep(3500); // non elegant, but necessary for test
        
        statistic = dataStore.getStatistics();
        assertTrue(statistic.getCount() == 0);        
    }
    
    @Test
    public void testTransactionWithSameTimestampsPostedThenCheckStatistic() {
        final long timestamp = TimeUtil.getTimestampCutOff() + 20000;
        
        Transaction transaction1 = new Transaction(Math.random(), timestamp);
        assertTrue(dataStore.addTransaction(transaction1));
        
        Transaction transaction2 = new Transaction(Math.random(), timestamp);
        assertTrue(dataStore.addTransaction(transaction2));
        
        double sum = transaction1.getAmount() + transaction2.getAmount();
        final Statistic statistic = dataStore.getStatistics();
        assertEquals(statistic.getCount(), 2L);
        assertEquals(statistic.getSum(), sum, DELTA);
        assertEquals(statistic.getAvg(), sum/2, DELTA);
        assertEquals(statistic.getMin(), Math.min(transaction1.getAmount(), transaction2.getAmount()), 0);
        assertEquals(statistic.getMax(), Math.max(transaction1.getAmount(), transaction2.getAmount()), 0);        
    }
    
    @Test
    public void testMultipleValidTransactionsThenCheckStatistic() {
        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;

        for (int i = 0; i < CNT_TRANSACTION_4_STATISTICS; i++) {
            final double amount = Math.random() * 1000;
            sum += amount;
            max = Math.max(max, amount);
            min = Math.min(min, amount);
            assertTrue(dataStore.addTransaction(new Transaction(amount, TimeUtil.getTimestampCutOff() + (i * 50))));
        }

        final double average = sum / CNT_TRANSACTION_4_STATISTICS;

        final Statistic statistic = dataStore.getStatistics();
        assertEquals(statistic.getCount(), CNT_TRANSACTION_4_STATISTICS);
        assertEquals(statistic.getSum(), sum, DELTA);
        assertEquals(statistic.getAvg(), average, DELTA);
        assertEquals(statistic.getMin(), min, 0);
        assertEquals(statistic.getMax(), max, 0);
    }    
    
    @Test
    public void testMixedTransactionsThenCheckStatistic() {
        double sum = 0;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        long count = 0;

        for (int i = 0; i < CNT_TRANSACTION_4_STATISTICS; i++) {
            final double amount = Math.random() * 1000;
            if (Math.random() < 0.5D) {
                assertFalse(dataStore.addTransaction(new Transaction(amount, TimeUtil.getTimestampCutOff() - 1000)));                
            } else {
                count++;
                sum += amount;
                max = Math.max(max, amount);
                min = Math.min(min, amount);
                assertTrue(dataStore.addTransaction(new Transaction(amount, TimeUtil.getTimestampCutOff() + (i * 50))));
            }
        }

        final double average = sum / count;

        final Statistic statistic = dataStore.getStatistics();
        assertEquals(statistic.getCount(), count);
        assertEquals(statistic.getSum(), sum, DELTA);
        assertEquals(statistic.getAvg(), average, DELTA);
        assertEquals(statistic.getMin(), min, 0);
        assertEquals(statistic.getMax(), max, 0);
    }
}
