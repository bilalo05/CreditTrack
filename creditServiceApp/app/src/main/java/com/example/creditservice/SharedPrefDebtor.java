package com.example.creditservice;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.sql.Timestamp;

public class SharedPrefDebtor {

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SharedPrefDebtor mInstance;
    private static Context mCtx;

    private RequestQueue requestQueue ;

    private SharedPrefDebtor(Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized SharedPrefDebtor getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefDebtor(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    private RequestQueue getRequestQueue ()
    {
        if (requestQueue == null){
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

public<T>  void addToRequestQueue(Request<T>request){
getRequestQueue().add(request);
}

}
