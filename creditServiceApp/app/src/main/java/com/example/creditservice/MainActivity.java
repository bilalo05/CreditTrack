package com.example.creditservice;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername,editTextMac;
    private Button buttonLogin ;
    private ProgressDialog progressDialog;
    Typeface myTypeface;


    private TextView textView ;
    String stringMac= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        myTypeface = Typeface.createFromAsset(getAssets(), "fonts/flat.ttf");


        textView = findViewById(R.id.mac);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
            return;
        }

        editTextUsername = (EditText) findViewById(R.id.username);
        //editTextUsername.setTypeface(myTypeface);
        //editTextPassword = (EditText) findViewById(R.id.admin_password);

        buttonLogin = (Button) findViewById(R.id.btn);
        buttonLogin.setTypeface(myTypeface);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ... ");

        buttonLogin.setOnClickListener(this);



        //enable button when field not empty

        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
String username = editTextUsername.getText().toString().trim();

buttonLogin.setEnabled(!username.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // text watcher end

    }

    private void userLogin(){
        final String username = editTextUsername.getText().toString().trim();
stringMac = "";
        //final String password = editTextPassword.getText().toString().trim();

        // GET MAC ADDRESS ****************************************************************************************

        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());



            for (NetworkInterface networkInterface:networkInterfaceList){
                if (networkInterface.getName().equalsIgnoreCase("wlan0")){
                    for (int i = 0 ; i<networkInterface.getHardwareAddress().length;i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xff);

                        if(stringMacByte.length() == 1){
                            stringMacByte = "0"+stringMacByte;
                        }

                        stringMac = stringMac + stringMacByte.toUpperCase()+":";
                    }
                    break;

                }
            }
      textView.setText(stringMac);
         //   Toast.makeText(this,"my mac = "+stringMac,Toast.LENGTH_LONG).show();


        } catch (SocketException e) {
            e.printStackTrace();
        }

        // GET MAC ADDRESS END ****************************************************************
        final String macAddress = textView.getText().toString().trim();
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                         progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){

                                SharedPrefManager.getInstance(getApplicationContext())
                                        .userLogin(
                                                obj.getInt("id"),
                                                obj.getString("username"),
                                                obj.getString("firstname"),
                                                obj.getString("lastname"),
                                                obj.getInt("storeId"),
                                                obj.getString("created_at")
                                                //obj.getInt("Debtor_id"),
                                                //obj.getString("Debtor_firstname"),
                                                //obj.getString("Debtor_lastname"),
                                                //obj.getInt("current")


                                        );

                               startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                         //      overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                finish();

                            }else{
//                                Toast.makeText(
//                                        getApplicationContext(),
//                                        obj.getString("message"),
//                                        Toast.LENGTH_LONG
//                                ).show();

                                if (obj.getString("message") != null) {
                                    StyleableToast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                                    progressDialog.dismiss();
//                        Toast.makeText(
//                                getApplicationContext(),
//                                error.getMessage(),
//                                Toast.LENGTH_LONG
//                        ).show();
                        if (error.getMessage() != null) {
                            StyleableToast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                        }
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<>();
             params.put("username",username);
             params.put("mac",macAddress);
             return params;
            }
        };

RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonLogin){
userLogin();
        }
    }



}
