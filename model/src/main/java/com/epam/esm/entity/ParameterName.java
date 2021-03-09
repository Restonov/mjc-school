package com.epam.esm.entity;

import org.apache.commons.lang3.math.NumberUtils;

public class ParameterName {

    private ParameterName() {
    }

    /**
     * Common params
     *
     */
    public static final int ZERO = NumberUtils.INTEGER_ZERO;
    public static final String CERTIFICATES_URL = "/certificates";
    public static final String TAGS_URL = "/tags";
    public static final String ID = "id";
    public static final String NAME = "name";

    /**
     * Gift certificate table params
     *
     */
    public static final String DESCRIPTION = "description";
    public static final String PRICE ="price";
    public static final String DURATION ="duration";
    public static final String CREATE_DATE ="create_date";
    public static final String LAST_UPDATE_DATE ="last_update_date";
    public static final String DESCENDING = "desc";

    /**
     * Gift certificate tag table
     *
     */
    public static final String GIFT_CERTIFICATE_ID = "gift_certificate_id";
    public static final String TAG_ID = "tag_id";
}
