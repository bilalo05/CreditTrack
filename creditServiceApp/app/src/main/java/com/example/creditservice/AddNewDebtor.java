package com.example.creditservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;
import com.tapadoo.alerter.Alerter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddNewDebtor extends AppCompatActivity implements View.OnClickListener {

    private Button back ;
    private TextView username ;

    //Register Fields

    private EditText fname,lname,current;
    private Button addBtn;
    private ProgressDialog progressDialog;
    private TextView storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_debtor);
        //FirebaseMessaging.getInstance().subscribeToTopic("topic"+SharedPrefManager.getInstance(this).getStoreId());

        // get fields
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        current = findViewById(R.id.currentDebit);

        addBtn = findViewById(R.id.addNewButton);


        fname.addTextChangedListener(textWatcher);
        lname.addTextChangedListener(textWatcher);



        progressDialog = new ProgressDialog(this);
        addBtn.setOnClickListener(this);

        //store id
        storeId = findViewById(R.id.storeId);
        if(storeId !=null) {
            storeId.setText(" "+SharedPrefManager.getInstance(this).getStoreId());
        }
//return to profile , username
        back = (Button) findViewById(R.id.buttonReturn);
        username = findViewById(R.id.username2);
        if(username != null) {
            username.setText(SharedPrefManager.getInstance(this).getAdminFirstName() + " " + SharedPrefManager.getInstance(this).getAdminLastName());
        }

        back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ProfileActivity.class));
      //  overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    //Send Token to Server

    private  void saveToken(){
        //get the token
        final String TAG = "MyFirebaseIIDService";
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                       final String mytoken = task.getResult().getToken();
                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
final String token2 = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
                        // Log and toast
                      //  token.setText(mytoken);
                        String msg = R.string.FCM_TOKEN+" "+mytoken;
                        Log.d(TAG, msg);
                      //  Toast.makeText(AddNewDebtor.this, msg, Toast.LENGTH_SHORT).show();

                        //Send the request

                        progressDialog.setMessage("الرجاء الانتظار ... ");
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Constants.URL_Add_TOKEN,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        try {
                                            JSONObject jsonObject= new JSONObject(response);
                                   //         Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                             //   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();

                            if (error.getMessage() != null) {
                                StyleableToast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                            }
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("token",mytoken);

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(AddNewDebtor.this);
                        requestQueue.add(stringRequest);
                    }
                });
        //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());


    }




    //Add New Debtor
    private void AddNewD(){
        final String firstname = fname.getText().toString().trim();
        final String lastname = lname.getText().toString().trim();
        final String debit = current.getText().toString().trim();
        final String store = storeId.getText().toString().trim();
      //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());

        progressDialog.setMessage("الرجاء الانتظار ... ");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_Add,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject= new JSONObject(response);
                     StyleableToast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG,R.style.ToastSuccess).show();
                            //       Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
           if (error.getMessage() != null) {
               StyleableToast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
           }
        //        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("fname",firstname);
                params.put("lname",lastname);
                params.put("current",debit);
                params.put("store_id",store);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //send the notification

    private  void sendNotification(){
        final String store = SharedPrefManager.getInstance(this).getStoreId()+"";
        Log.d("STORE--------------","-------------->"+store);
        final String fullname = SharedPrefManager.getInstance(this).getAdminFirstName() +" "+SharedPrefManager.getInstance(this).getAdminLastName();
        final String message = fullname+" أضاف زبونا جديدا";
        final String title = "إضافة زبون";
        //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());

        progressDialog.setMessage("الرجاء الانتظار ... ");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {

                       /*     Toast.makeText(getApplicationContext(),
                                    "nigga",Toast.LENGTH_LONG).show();*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
          if (error.getMessage() != null) {
              StyleableToast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
          }
                //    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("title",title);
                params.put("content",message);
                params.put("id",store);


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Alerter.create(this)
                .setTitle(title)
                .setText(message)
                .setIcon(R.drawable.icon)
                .setBackgroundColorRes(R.color.pinkGlamour)
                .setDuration(5000)
                .enableSwipeToDismiss()
                .show();
    }



    @Override
    public void onClick(View v) {

        if (v == back){

            startActivity(new Intent(this,ProfileActivity.class));
        //    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }else {
            if (v == addBtn){
                saveToken();
                 AddNewD();
              sendNotification();

            }
        }
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String firstname = fname.getText().toString().trim();
            String lastname = lname.getText().toString().trim();

            addBtn.setEnabled(!firstname.isEmpty() && !lastname.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
