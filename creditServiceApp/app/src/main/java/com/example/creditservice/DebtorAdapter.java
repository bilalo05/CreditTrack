package com.example.creditservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtorAdapter extends RecyclerView.Adapter<DebtorAdapter.DebtorViewHolder> implements Filterable {

      private Context context;
      private List<Debtor> debtorList;
      List<Debtor> debtorListFull;

    private ProgressDialog progressDialog;


    public DebtorAdapter(Context context, List<Debtor> debtorList) {
        this.context = context;
        this.debtorList = debtorList;
        debtorListFull = new ArrayList<>(debtorList);
    }

    @NonNull
    @Override
    public DebtorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debor_list,null);
        return new DebtorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DebtorViewHolder holder, int position) {

        final Debtor debtor = debtorList.get(position);

        holder.textViewFullName.setText(debtor.getFirstName()+" "+debtor.getLastName());
        holder.textViewCredit.setText("القيمة الحالية للدين : "+debtor.getCredit()+" دج ");
        holder.textViewDate.setText(debtor.getStatus());
        if (debtor.getCredit() == 0){
            holder.textViewStatus.setText("لا يملك ديون");
            holder.textViewStatus.setBackgroundColor(Color.parseColor("#2ed573"));
        }else{
                holder.textViewStatus.setText("  مستدين");
                holder.textViewStatus.setBackgroundColor(Color.parseColor("#eb4d4b"));

        }
       // holder.textViewStatus.setText("Cool");
        // Go to a specific item
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,DebtorDetails.class);
                i.putExtra("myObj",debtor);
                v.getContext().startActivity(i);


            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToken();
               deleteDebtor(debtor.getId());
                sendNotification("حذف زبون"," قام بحذف الزبون : ",debtor);
                Intent i = new Intent(context,ProfileActivity.class);
                v.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return debtorList.size();
    }


    //Class 2 ViewHolder

    class DebtorViewHolder extends RecyclerView.ViewHolder {

   TextView textViewFullName,textViewDate,textViewStatus,textViewCredit;
   RelativeLayout relativeLayout ;
   Button delete ;

        //CLASS 2 CONSTRUCTOR
        public DebtorViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewFullName = itemView.findViewById(R.id.textViewFullName);
            textViewCredit = itemView.findViewById(R.id.textViewCredit);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);

            delete = itemView.findViewById(R.id.deleteDebtor);

            relativeLayout = itemView.findViewById(R.id.myRelativeLayout);



        }

    }


    @Override
    public Filter getFilter() {
        return DebtorFilter;
    }

    private Filter DebtorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Debtor> filteredList = new ArrayList<>();

            if( constraint == null || constraint.length() == 0){
                filteredList.addAll(debtorListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for( Debtor debtorItem : debtorListFull){
                    if (debtorItem.getFirstName().toLowerCase().contains(filterPattern)){
                        filteredList.add(debtorItem);
                    }
                }

            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            debtorList.clear();
            debtorList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };



    //delete debtor

    public void deleteDebtor(int id){
        final String url = Constants.ipAddress+"/android/v1/DeleteDebtor.php?id="+id;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject= (JSONObject) new JSONObject(response);
                    StyleableToast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG,R.style.ToastSuccess).show();
//                    Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    StyleableToast.makeText(context, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                }
                //                Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

        Volley.newRequestQueue(context).add(request);

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
                        //      SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences(getString(R.string.FCM_PREF), Context.MODE_PRIVATE);
                        //    final String token2 = sharedPreferences.getString(getString(R.string.FCM_TOKEN),"");
                        // Log and toast
                        //  token.setText(mytoken);
                        String msg = R.string.FCM_TOKEN+" "+mytoken;
                        Log.d(TAG, msg);
                    //    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        //Send the request


                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Constants.URL_Add_TOKEN,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject= new JSONObject(response);
                                 //           Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.getMessage() != null) {
                                    StyleableToast.makeText(context, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                                }
                                //                                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("token",mytoken);

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(context);
                        requestQueue.add(stringRequest);
                    }
                });
        //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());


    }


    //send the notification

    private  void sendNotification(String title2 , String content, Debtor debtor){
        final String fullname = SharedPrefManager.getInstance(context).getAdminFirstName() +" "+SharedPrefManager.getInstance(context).getAdminLastName();
        final String message = fullname+" "+content+" "+debtor.getFirstName()+" "+debtor.getLastName();
        final String title = title2;
        final  String store =   SharedPrefManager.getInstance(context).getStoreId()+"";
        //  final String title = "إضافة زبون";
        //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_NOTIFICATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            StyleableToast.makeText(context,"تم التحديث بنجاح",Toast.LENGTH_LONG,R.style.ToastSuccess).show();
//                            Toast.makeText(context,
//                                    "تم التحديث بنجاح",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    StyleableToast.makeText(context, error.getMessage(), Toast.LENGTH_LONG, R.style.ToastFailed).show();
                }
                //                Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
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

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
