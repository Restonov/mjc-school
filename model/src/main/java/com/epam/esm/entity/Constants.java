package com.epam.esm.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    /**
     * Api URLs
     *
     */
    public static final String AUTH_URL = "/api/auth";
    public static final String CERTIFICATES_URL = "/api/certificates";
    public static final String CERTIFICATE_BY_ID_URL = "/api/certificates/{certificateId}";
    public static final String TAGS_URL = "/api/tags";
    public static final String USERS_URL = "/api/users";
    public static final String AUTH_SIGNUP_URL = "/api/auth/signup";
    public static final String CREATE_USER_ORDER_URL = "/api/users/{userId}/orders";
    public static final String MOST_PROFIT_TAG_URL = "/api/tags/profit";
    public static final String AUTH_GENERATE_TOKEN_URL = "/api/auth/token";

    /**
     * Data types
     *
     */
    public static final String APPLICATION_JSON_PATCH = "application/json-patch+json";
    public static final String APPLICATION_JSON_UTF8 = "application/json;charset=UTF-8";

    /**
     * Common params
     */
    public static final String NAME = "name";
    public static final String USER = "USER";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String ADMINISTRATOR_AUTHORITY = "[ROLE_ADMINISTRATOR]";
    public static final String CREATE_DATE = "createDate";

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
    public static final String DELETE_TAG = "deleteTag";
    public static final String MOST_PROFITABLE_TAG = "mostProfitableTag";
    // CertificateController
    public static final String CREATE_CERTIFICATE = "createCertificate";
    public static final String ALL_CERTIFICATES = "findAllCertificates";
    public static final String DELETE_CERTIFICATE = "deleteCertificate";

    //Security
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 18000000;
    public static final String SIGNING_KEY = "rstnv0211";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String OKTA_PREFIX = "Okta ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_KEY = "scopes";
    public static final String GENERATED_JWT = "Generated JWT";
    public static final String TOKEN_GENERATED_MESSAGE = "Token was successfully generated";
}
