package com.kfpun.goldenear.app;

/**
 * Created by jp on 5/16/14.
 */
public class Constants {
    public static final String MESSAGE_TAG = "MESSAGE";
    public static final String FRAGMENT_TAG = "FRAGMENT";
    public static final String EVENT_TAG = "EVENT";
    public static final String JSON_TAG = "JSON";
    public static final String HTTP_TAG = "HTTP";
    public static final String ION_TAG = "ION";
    public static final String FILE_TAG = "FILE";
    public static final String GPS_TAG = "GPS";
    public static final String IMAGE_TAG = "IMAGE";
    public static final String PROCESS_TAG = "PROCESS";

    public static Integer[] getMedias() {
        return new Integer[] {
            R.raw.onclassical_low,
            R.raw.onclassical_low,
            R.raw.onclassical_high,
            R.raw.onclassical_high,
        };
    }
}
