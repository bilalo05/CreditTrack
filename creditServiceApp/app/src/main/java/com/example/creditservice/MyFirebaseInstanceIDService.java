package com.example.creditservice;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tapadoo.alerter.Alerter;

import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);



        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
     //   sendRegistrationToServer(token);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.FCM_TOKEN),token);
        editor.commit();

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        // create Notification
        Log.d(TAG, "Refreshed tokennnnnn: " + SharedPrefManager.getInstance(this).getAdminLastName() );
//        Toast.makeText(this,"stooooore ="+SharedPrefManager.getInstance(this).getAdminLastName(),Toast.LENGTH_LONG).show();

        FirebaseMessaging.getInstance().subscribeToTopic("topic"+SharedPrefManager.getInstance(this).getStoreId());

        //you can get your text message here.
      //  String text= data.get("text");
        if( remoteMessage.getNotification() != null){
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle("تحديث بالمتجر");
            notificationBuilder.setContentText(remoteMessage.getNotification().getBody());
            notificationBuilder.setSmallIcon(R.drawable.icon);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());


        }

    }

    //    @Override
//    public void onTokenRefresh() {
//
//        //Getting registration token
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//
//        //Displaying token on logcat
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
//
//        //calling the method store token and passing token
//        storeToken(refreshedToken);


    private void sendRegistrationToServer(String token) {
        //we will save the token in sharedpreferences later
        Toast.makeText(this,"yess "+token,Toast.LENGTH_LONG).show();

    }



/**
    private void storeToken(String token) {
        //we will save the token in sharedpreferences later
        SharedPrefDebtor.getInstance(getApplicationContext()).saveDeviceToken(token);
    }**/
}
