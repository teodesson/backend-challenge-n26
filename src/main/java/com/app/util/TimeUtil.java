package com.app.util;

import com.app.config.AppConfig;

/**
 *
 * @author Desson Ariawan <teodesson@gmail.com>
 */
public class TimeUtil {

    public static long getTimestampCutOff() {
        return System.currentTimeMillis() - AppConfig.MS_2_RECYCLE_A_BUCKET;
    }
}
