package com.epam.esm.entity;

public final class ParameterName {

    private ParameterName() {
    }

    /**
     * Common params
     */
    public static final String CERTIFICATES_URL = "/certificates";
    public static final String TAGS_URL = "/tags";
    public static final String USERS_URL = "/users";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String KEY_WORD = "keyWord";
    public static final String USER = "user";
    public static final String APPLICATION_JSON_PATCH = "application/json-patch+json";

    /**
     * Gift certificate table params
     */
    public static final String NAME_DESC = "name_desc";
    public static final String DATE_ASC = "date_asc";
    public static final String DATE_DESC = "date_desc";
    public static final String DESCRIPTION = "description";

    /**
     * Gift certificate tag table
     */
    public static final String TAG_NAME = "tagName";
    public static final String TAGS = "tags";

    /**
     * HATEOAS links
     */
    // UserController
    public static final String CREATE_ORDER = "createOrder";
    public static final String USER_ORDER = "userOrder";
    public static final String USER_ORDERS = "userOrders";
    public static final String ALL_USERS = "allUsers";
    // TagController
    public static final String CREATE_TAG = "createTag";
    public static final String ALL_TAGS = "allTags";
    public static final String DELETE_TAG = "deleteTag";
    public static final String MOST_PROFITABLE_TAG = "mostProfitableTag";
    // CertificateController
    public static final String CREATE_CERTIFICATE = "createCertificate";
    public static final String ALL_CERTIFICATES = "allCertificates";
    public static final String ALL_SORTED_CERTIFICATES = "allSortedCertificates";
    public static final String DELETE_CERTIFICATE = "deleteCertificate";
    public static final String BY_TAG_NAME = "findByTagName";
    public static final String BY_KEYWORD = "findByKeyWord";
    public static final String SORT_BY = "sortBy";
}
