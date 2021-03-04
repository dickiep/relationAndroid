package com.dickies.android.relationbn.utils;

public class Config {
    //URL to our login.php file
    public static final String LOGIN_URL = "http://pdickie01.web.eeecs.qub.ac.uk/relation/v1/sign.php";

    //Keys for email and password as defined in our $_POST['key'] in login.php
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    //If server response is equal to this that means login is successful
    public static final String LOGIN_SUCCESS = "user login success";

    public static final String ADMIN_LOGIN_SUCCESS = "admin login success";

    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

    public static final String ADMIN_LOGGEDIN_SHARED_PREF = "loggedin";

    private boolean ADMIN;


    public boolean isADMIN() {
        return ADMIN;
    }

    public void setADMIN(boolean ADMIN) {
        this.ADMIN = ADMIN;
    }
}