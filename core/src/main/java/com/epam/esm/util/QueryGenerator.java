package com.epam.esm.util;

/**
 * Create %text% for ILIKE SQL Query
 */
public class QueryGenerator {
    private static final String PERCENT_SIGN = "%";

    private QueryGenerator() {}

    public static String generateKeyWord(String text) {
        StringBuilder builder = new StringBuilder();
        builder.append(PERCENT_SIGN).append(text).append(PERCENT_SIGN);
        return builder.toString();
    }
}
