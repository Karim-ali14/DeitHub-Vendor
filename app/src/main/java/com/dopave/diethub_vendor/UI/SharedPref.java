package com.dopave.diethub_vendor.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

public class SharedPref {
    // LogCat tag
    private static String TAG = SharedPref.class.getSimpleName();

    // Shared Preferences
    private SharedPreferences pref;

    private SharedPreferences.Editor editor;
    private Context _context;

    // Shared pref mode
    private int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "diethub";

    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    private static final String KEY_USER_ID = "USERID";

    private static final String KEY_USER_NAME = "USERNAME";

    private static final String TAG_TOKEN = "tagtoken";

    public static final String KEY_USER_EMAIL = "userEmail";

    public static final String KEY_USER_PHONE = "userPhone";


    public static final String TAG_IMAGE_PROFILE = "avatar";

    public static final String TAG_Langu = "Langu";

    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        editor.apply();
    }

    public void setLogin(boolean isLoggedIn, String userID, String userName,String userEmail ,
                         String userTocken ,String userPhone) {

        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

        editor.putString(KEY_USER_ID, userID);

        editor.putString(KEY_USER_NAME, userName);

        editor.putString(KEY_USER_EMAIL,userEmail);

        editor.putString(TAG_TOKEN,userTocken);

        editor.putString(KEY_USER_PHONE,userPhone);

        // commit changes
        editor.commit();

    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUserName(String username){
        editor.putString(KEY_USER_NAME, username);
        // commit changes
        editor.commit();
    }
    public void setUserEmail(String userEmail){
        editor.putString(KEY_USER_EMAIL, userEmail);
        // commit changes
        editor.commit();
    }

    public void setUserTocken(String userTocken){
        editor.putString(TAG_TOKEN, userTocken);
        // commit changes
        editor.commit();
    }
    public void setUserID(Integer userID){
        editor.putInt(KEY_USER_NAME, userID);
        // commit changes
        editor.commit();
    }
    public void setTagImageProfile(String userImage){
        editor.putString(TAG_IMAGE_PROFILE, userImage);
        // commit changes
        editor.commit();
    }
    public void setUserPhone(String userPhone){
        editor.putString(KEY_USER_PHONE, userPhone);
        // commit changes
        editor.commit();
    }

    public String getUserId(){
        return pref.getString(KEY_USER_ID, "empty");
    }

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, "empty");
    }

    public  String getTagToken() {
        return pref.getString(TAG_TOKEN,"empty");
    }

    public  String getKeyUserEmail() {
        return pref.getString(KEY_USER_EMAIL,"empty");
    }

    public String getTagImageProfile() {
        return pref.getString(TAG_IMAGE_PROFILE,"empty");
    }

    public String getLagu() {
        return pref.getString(TAG_Langu,"empty");
    }

    public String getKeyUserPhone() {
       return pref.getString(KEY_USER_PHONE,"empty");
    }


    public void setLangu(String Langu){
        editor.putString(TAG_Langu, Langu);
        // commit changes
        editor.commit();
    }


    public void setTagImageProfile(Bitmap decodedImage) {

    }
}
