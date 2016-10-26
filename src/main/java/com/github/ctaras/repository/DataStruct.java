package com.github.ctaras.repository;

import org.springframework.stereotype.Component;

@Component
public class DataStruct {

    public final String USER_TABLE = "user";
    public final String CONTACT_TABLE = "contact";

    public final String USER_ID = "user_id";
    public final String USER_LOGIN = "login";
    public final String USER_PASSWD = "password";
    public final String USER_FULL_NAME = "full_name";

    public final String CON_ID = "contact_id";
    public final String CON_LAST_NAME = "last_name";
    public final String CON_FIRST_NAME = "first_name";
    public final String CON_MIDDLE_NAME = "middle_name";
    public final String CON_MOB_PHONE_NUM = "mobile_phone_number";
    public final String CON_HOME_PHONE_NUM = "home_phone_number";
    public final String CON_ADDRESS = "address";
    public final String CON_EMAIL = "email";
}
