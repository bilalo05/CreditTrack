package com.example.creditservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.muddzdev.styleabletoast.StyleableToast;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    Button back ;
    int storeId=  0;
    TextView username,totalD,noDebtText,hasDebtText,storeName,period,months;

    String url = Constants.ipAddress+"/android/v1/fcmInsert.php";


    Button gettoken;

    TextView token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        JodaTimeAndroid.init(this);

        getStore();

        Intent i = getIntent();
        final int total = i.getIntExtra("total",0);
        final int noDebt = i.getIntExtra("noDebt",0);
        final int hasDebt = i.getIntExtra("hasDebt",0);


        totalD = findViewById(R.id.totalText);
        totalD.setText(totalD.getText().toString()+"\n"+total+" دج ");

        noDebtText = findViewById(R.id.noDebtText);
        noDebtText.setText(noDebtText.getText().toString()+"\n"+noDebt+" زبون ");

        hasDebtText = findViewById(R.id.debtedText);
        hasDebtText.setText(hasDebtText.getText().toString()+"\n"+hasDebt+" زبون ");

        back = findViewById(R.id.buttonReturnProfile);
        back.setOnClickListener(this);

        storeName = findViewById(R.id.storeName);

        period = findViewById(R.id.period);
        months = findViewById(R.id.months);
        //period.setText(SharedPrefManager.getInstance(this).getAdminCreatedAt());
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        try {
           String created_date = SharedPrefManager.getInstance(this).getAdminCreatedAt();
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(sdf.parse(created_date));// all done

            //end date
            int endDay = cal.get(Calendar.DAY_OF_MONTH);
            int endMonth = cal.get(Calendar.MONTH)+7;
            if (endMonth>12){
                endMonth = endMonth-12;
            }

            //current date
            Calendar now = Calendar.getInstance();
           // int year = now.get(Calendar.YEAR);
            int currentMonth = now.get(Calendar.MONTH) + 1; // Note: zero based!
            int currentDay = now.get(Calendar.DAY_OF_MONTH);

            int diff_m = endMonth-currentMonth;
            if (diff_m<0){
               diff_m= 12-currentMonth + endMonth;
            }
            int diff_d = endDay - currentDay ;
            if (diff_d<0){
                diff_d = 30-currentDay +endDay;
                diff_m = diff_m-1;
            }

            period.setText(String.valueOf(diff_m));

            if ( diff_d == 0 && diff_m == 0){
                SharedPrefManager.getInstance(this).logout();
                finish();
                startActivity(new Intent(this, MainActivity.class));
            }

            if (diff_m>9){
                if (diff_d >9) {
                    months.setText("شهرا" + "  و " + diff_d + " يوما ");
                }else{
                    if (diff_d>2){
                        months.setText("شهرا" + "  و " + diff_d + " أيام ");
                    }else{
                        if (diff_d == 2){
                            months.setText("شهرا" + "  و " +  " يومان ");
                        }else {
                            months.setText("شهرا" + "  و "  + " يوم واحد ");
                        }
                    }
                }
            }else {
               if (diff_m>2){
                   months.setText("أشهر" + "  و " + diff_d + " يوما ");
               }else {
                   if (diff_m==2) {
                       period.setText("شهران");
                       months.setText( "  و " + diff_d + " يوما ");
                   }else {
                       period.setText("شهر واحد و");
                       months.setText("  " + diff_d + " يوما ");
                   }
               }
            }

            if (diff_m == 0 ){
                period.setText("");
                months.setTextColor(getResources().getColor(R.color.carmin));
            }

        //  Toast.makeText(this,"Day = "+cal.get(Calendar.DAY_OF_MONTH)+"Month = "+cal.get(Calendar.MONTH)+" Year= "+cal.get(Calendar.YEAR),Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //store id
        storeId = SharedPrefManager.getInstance(this).getStoreId();

//, username
        username = findViewById(R.id.debtorNameProfile);
        if(username != null) {
            username.setText(SharedPrefManager.getInstance(this).getAdminFirstName() + " " + SharedPrefManager.getInstance(this).getAdminLastName());
        }

        //firebase token

       /* gettoken = findViewById(R.id.displaytoken);
        token = findViewById(R.id.token);*/
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ProfileActivity.class));
     //   overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    @Override
    public void onClick(View v) {
        if (v==back ){
            startActivity(new Intent(this,ProfileActivity.class));
     //       overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }

    }

    public void getStore(){


            final String myurl =
                    Constants.ipAddress+"/android/v1/GetStore.php?store_id="+SharedPrefManager.getInstance(this).getStoreId();
            StringRequest request = new StringRequest(Request.Method.GET, myurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray debtors = new JSONArray(response);


                            JSONObject debtorObject = debtors.getJSONObject(0);

                            int id = debtorObject.getInt("id");
                            String store_name = debtorObject.getString("store_name");
                            String created_at = debtorObject.getString("created_at");
                            String date = debtorObject.getString("updated_at");

                            //Date dates = new Date(System.currentTimeMillis());
                            //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                            //Locale.ENGLISH);
                            //String var = dateFormat.format(dates);

 storeName.setText(store_name);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getMessage() != null) {
                                StyleableToast.makeText(EditProfile.this, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                            }
                            //
//                            Toast.makeText(EditProfile.this,error.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(this).add(request);

    }
}
