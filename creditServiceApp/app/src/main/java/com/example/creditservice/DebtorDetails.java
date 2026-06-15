package com.example.creditservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtorDetails extends AppCompatActivity implements View.OnClickListener {

   private TextView fullname,total ;
   private Button  back,addnew;

   //Recycle view
    RecyclerView recyclerView;
    DebtAdapter debtAdapter;

    Debtor d ;

    private ProgressDialog progressDialog;
    List<Debt> debtList;
    int totalDebt=0;

    SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtor_details);



        fullname = findViewById(R.id.textViewFullName2);
        total = findViewById(R.id.total2);

        back = findViewById(R.id.buttonReturn);
        addnew = findViewById(R.id.addNewDebt);

        progressDialog = new ProgressDialog(this);

        back.setOnClickListener(this);



   //  getIntent().getSerializableExtra("myobj");

        Intent i = getIntent();
        final Debtor debtor = (Debtor) i.getSerializableExtra("myObj");

        if (fullname != null && debtor !=null) {
    fullname.setText(debtor.getFirstName() + " " + debtor.getLastName());
    total.setText(debtor.getCredit()+" دج");
            //final String url ="http://192.168.1.4/android/v1/DisplayDebts.php?debtor_id="+debtor.getId();
            //refresh
            refreshLayout = findViewById(R.id.refreshLayout);
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadDebts(debtor.getId());
                }
            });


            //adding some items to our list
            loadDebts(debtor.getId());

}

        d = debtor;

        //add new debt intent
        addnew.setOnClickListener(this);



//Recycle view

        debtList = new ArrayList<>();

recyclerView = findViewById(R.id.recycleViewDebts);
recyclerView.setHasFixedSize(true);

recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //adding some items to our list

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ProfileActivity.class));
    }


    private void loadDebts( int id){
        if (debtList != null) {
            debtList.clear();
        }
        totalDebt = 0;
        refreshLayout.setRefreshing(true);
      final String url =Constants.ipAddress+"/android/v1/DisplayDebts.php?debtor_id="+id;

      StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
          @Override
          public void onResponse(String response) {
              try {
                  JSONArray debts = new JSONArray(response);

                  for (int i =  0 ; i<debts.length();i++){
                      JSONObject debtObject = debts.getJSONObject(i);

                      int id = debtObject.getInt("debt_id");
                      String title = debtObject.getString("product_name");
                      int price = debtObject.getInt("price");
                      String image = debtObject.getString("product_picture");
                      String date1 = debtObject.getString("created_at");
                      String date2 = debtObject.getString("updated_at");

                      totalDebt = totalDebt+price;
                      Debt debt = new Debt(id,title,price,image,date1,date2);
                      debtList.add(debt);

                  }
                  total.setText(""+totalDebt+" دج ");
                  updateDebt();


                  debtAdapter = new DebtAdapter(DebtorDetails.this,debtList);
                  recyclerView.setAdapter(debtAdapter);

              } catch (JSONException e) {
                  refreshLayout.setRefreshing(false);
                  e.printStackTrace();
              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
              refreshLayout.setRefreshing(false);
              if (error.getMessage() != null) {
                  StyleableToast.makeText(DebtorDetails.this, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
              }
//              Toast.makeText(DebtorDetails.this,error.getMessage(),Toast.LENGTH_SHORT).show();

          }
      });
        refreshLayout.setRefreshing(false);
        Volley.newRequestQueue(this).add(request);
    }


    public void updateDebt(){

            final String debtorDebt = Integer.toString(totalDebt).trim();
            final String debtorId = Integer.toString(d.getId()).trim();

            //  final int debitC = Integer.parseInt(debit);
            //int store_id= Integer.parseInt(storeId.getText().toString().trim());

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_DEBT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject= new JSONObject(response);
                   //             Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                refreshLayout.setRefreshing(false);
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    refreshLayout.setRefreshing(false);
                    progressDialog.hide();
         if (error.getMessage() != null) {
             StyleableToast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
         }
                 //   Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<>();
                    params.put("id",debtorId);
                    params.put("current_debit",debtorDebt);


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    @Override
    public void onClick(View v) {
        if(v == back){
            startActivity(new Intent(this,ProfileActivity.class));
        }else{
            if (v==addnew){
                Intent i = new Intent(this,AddNewDebt.class);
                i.putExtra("debtor",d);
                v.getContext().startActivity(i);
            }

        }

    }
}
