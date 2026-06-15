package com.example.creditservice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
private  long backPressedTime;
    int total,noDebt,hasDebt = 0;
    Typeface myTypeface;
    Typeface myTypefaceBold;
    private ProgressDialog progressDialog;
    private TextView username;
    //private TextView username , firstName , lastName ;
    TextView textViewFullName,textViewDate,textViewStatus,textViewCredit;
    ImageView profile ;



    private Button buttonLog, btn;
    private TextView totalD, ids;


    //recycleview

    RecyclerView recyclerView;
    DebtorAdapter adapter;

    List<Debtor> debtorList;
    List<Debtor> debtorListFull;
    SearchView search ;
    RecyclerView.Adapter madapter;

    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        int topic = SharedPrefManager.getInstance(this).getStoreId();
        FirebaseMessaging.getInstance().subscribeToTopic(topic+"");
        Log.d("STORE--------------","-------------->"+topic);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);



        username = findViewById(R.id.username);
        buttonLog = (Button) findViewById(R.id.buttonLogout);
        btn = (Button) findViewById(R.id.addNew);
        profile = findViewById(R.id.profile_pic);
        buttonLog.setOnClickListener(this);
        myTypeface = Typeface.createFromAsset(getAssets(), "fonts/flat.ttf");
        myTypefaceBold = Typeface.createFromAsset(getAssets(), "fonts/cairo_bold.ttf");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait ... ");

        //go to profile
        profile.setOnClickListener(this);

        //Recycle View
        textViewFullName = findViewById(R.id.textViewFullName);
        textViewCredit = findViewById(R.id.textViewCredit);
        textViewDate = findViewById(R.id.textViewDate);
        textViewStatus = findViewById(R.id.textViewStatus);



        //Recycle view initialise
        debtorList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //refresh
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDebtors();
            }
        });


        //adding some items to our list
        loadDebtors();


        //add new debtor
        btn.setOnClickListener(this);

        // SEARCH VIEW
        search = findViewById(R.id.search);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(adapter != null) {
                    adapter.getFilter().filter(newText);
                }else {
                    search.clearFocus();
                }
                return false;
            }
        });


        //SEARCH VIEW END


       // lv = findViewById(R.id.idList);
//

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        ids = (TextView) findViewById(R.id.idS);
        username.setTypeface(myTypefaceBold);
        username.setText(SharedPrefManager.getInstance(this).getAdminFirstName() + " " + SharedPrefManager.getInstance(this).getAdminLastName());
        ids.setText(" "+SharedPrefManager.getInstance(this).getStoreId());




        //Test if the client has an active account

        String created_date = SharedPrefManager.getInstance(this).getAdminCreatedAt();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            cal.setTime(sdf.parse(created_date));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }

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


        if ( diff_d == 0 && diff_m == 0){
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        //account verification end


    }

    @Override
    public void onBackPressed() {
        if (backPressedTime +2000 > System.currentTimeMillis()){
              super.onBackPressed();
              return;
        }else {
           // StyleableToast.makeText(getBaseContext(),,Toast.LENGTH_SHORT);
           StyleableToast.makeText(getBaseContext(),"اضغط مرة اخرى للمغادرة",Toast.LENGTH_SHORT,R.style.exit).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    private void loadDebtors(){
       // refreshLayout.isRefreshing();
        if (debtorList != null) {
            debtorList.clear();
        }
        total = 0;
        refreshLayout.setRefreshing(true);
        final String myurl =
                Constants.ipAddress+"/android/v1/GetDebtors.php?store_id="+SharedPrefManager.getInstance(this).getStoreId();
        StringRequest request = new StringRequest(Request.Method.GET, myurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray debtors = new JSONArray(response);

                    for (int i = 0 ; i<debtors.length();i++){
                        JSONObject debtorObject = debtors.getJSONObject(i);

                        int id = debtorObject.getInt("id");
                        String firstname = debtorObject.getString("firstname");
                        String lastname = debtorObject.getString("lastname");
                        int current = debtorObject.getInt("current_debit");
                        String date = debtorObject.getString("updated_at");
                        total = total + current;
                        //Date dates = new Date(System.currentTimeMillis());
                        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",
                                //Locale.ENGLISH);
                        //String var = dateFormat.format(dates);

                        if (current == 0 ){
                            noDebt = noDebt +1;
                        }else{
                            hasDebt = hasDebt+1;
                        }



                        Debtor debtor = new Debtor(id,firstname,lastname,current,date);
                        debtorList.add(debtor);
                    }
                    totalD = (TextView) findViewById(R.id.total);
                    totalD.setText(total+"دج");

                    adapter = new DebtorAdapter(ProfileActivity.this,debtorList);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    refreshLayout.setRefreshing(false);
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refreshLayout.setRefreshing(false);
                if (error.getMessage() != null) {
                    StyleableToast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                }
                //              Toast.makeText(ProfileActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(request);
        refreshLayout.setRefreshing(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public void onClick(View v) {
        if(v==buttonLog) {
            SharedPrefManager.getInstance(this).logout();
            finish();
            startActivity(new Intent(this, MainActivity.class));
            //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
            if(v==btn){
                startActivity(new Intent(this,AddNewDebtor.class));
             //   overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

        }
            if (v== profile){
                startActivity(new Intent(this,EditProfile.class).putExtra("total",total).putExtra("noDebt",noDebt).putExtra("hasDebt",hasDebt));
           //     overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }


    }

}


