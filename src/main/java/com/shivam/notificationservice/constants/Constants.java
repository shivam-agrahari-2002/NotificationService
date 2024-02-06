package com.shivam.notificationservice.constants;

public interface Constants {
    static final String API_URL = "https://api.imiconnect.in/resources/v1/messaging";
    static final String API_KEY = "c0c49ebf-ca44-11e9-9e4e-025282c394f2";
    static final String SET_KEY = "blacklist-phone-numbers";
    static final int REDIS_TTL = 18000;
    static final String HOST = "localhost";
    static final int REDIS_PORT = 6379;
    static final String KAFKA_TOPIC = "notifications";
    static final String INTERNAL_API_AUTH_HEADER = "key";
    static final String INTERNAL_API_KEY = "meesho@123";
    static final String KAFKA_GROUP_ID = "notification";
    static final String INVALID_REQUEST = "INVALID_REQUEST";
}
