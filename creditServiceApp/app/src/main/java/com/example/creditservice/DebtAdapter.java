package com.example.creditservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtAdapter extends RecyclerView.Adapter<DebtAdapter.DebtViewHolder>  {

    int finalHeight,finalWidth;

    private Context context;
    private List<Debt> debtList;
    public DebtAdapter(Context context, List<Debt> debtList) {
        this.context = context;
        this.debtList = debtList;
    }

    @NonNull
    @Override
    public DebtViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.debt_list,null);
        return new DebtAdapter.DebtViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final DebtViewHolder holder, int position) {
       final Debt debt = debtList.get(position);

        holder.productName.setText(debt.getProductLabel());
        holder.debtDate.setText(debt.getProductStatusCreated());
        holder.debtPrice.setText(holder.debtPrice.getText()+" "+debt.getProductDebt()+" دج");
       // holder.imageViewP.setImageDrawable(context.getResources().getDrawable(debt.getProductImage()));

        if(debt.getProductDebt() == 0 ){
            holder.debtStatus.setBackgroundColor(Color.parseColor("#03DAC5"));
            holder.debtStatus.setText("مسدد");
        }else{
            holder.debtStatus.setBackgroundColor(Color.parseColor("#eb4d4b"));
            holder.debtStatus.setText("غير مسدد");

        }



        Glide.with(context)
                .load(debt.getProductImage())
                .into(holder.imageViewP);


        //show update panel
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(debt.getProductDebt() == 0){
                    StyleableToast.makeText(context,"لا يوجد دين في هذا المنتوج !",Toast.LENGTH_LONG,R.style.ToastFailed).show();
                   // Toast.makeText(context,"لا يوجد دين في هذا المنتوج !",Toast.LENGTH_LONG).show();
                }else {
                    holder.layout.setVisibility(v.VISIBLE);
                }

            }
        });
        holder.progressDialog = new ProgressDialog(context);        //update
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int edittxtdebt = Integer.parseInt(holder.debt.getText().toString().trim());
                int newdebt = debt.getProductDebt()- edittxtdebt;
                if(newdebt <0){
                    StyleableToast.makeText(context,"لقد قمت بادخال قيمة اكبر من الدين الحالي !",Toast.LENGTH_LONG,R.style.ToastFailed).show();
              //     Toast.makeText(context,"لقد قمت بادخال قيمة اكبر من الدين الحالي !",Toast.LENGTH_LONG).show();
                }else{
                    saveToken();
                    String debtproduct = debt.getProductLabel();
                    update(debt.getId(),newdebt);
                    sendNotification("تحديث الدين"," قام بتحديث دين المنتوج : ",debtproduct);
                    Intent i = new Intent(context,ProfileActivity.class);


                    v.getContext().startActivity(i);

                }

            }
        });
//delete
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToken();
                String debtproduct = debt.getProductLabel();
                delete(debt.getId());
                sendNotification("حذف الدين"," قام بحذف دين المنتوج : ",debtproduct);
                Intent i = new Intent(context,ProfileActivity.class);
                v.getContext().startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return debtList.size();
    }

    class DebtViewHolder extends  RecyclerView.ViewHolder{

        TextView productName,debtDate,debtStatus,debtPrice;
        ImageView imageViewP;
         Button update,delete,show;
         LinearLayout layout;
         EditText debt;
        private ProgressDialog progressDialog;


        public DebtViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            debtDate = itemView.findViewById(R.id.debtDate);
            debtStatus = itemView.findViewById(R.id.debtStatus);
            debtPrice = itemView.findViewById(R.id.debtPrice);
            imageViewP = itemView.findViewById(R.id.imageViewP);
            update = itemView.findViewById(R.id.updateDebt);
            delete = itemView.findViewById(R.id.deleteDebt);
            show = itemView.findViewById(R.id.showUpdate);
            layout= itemView.findViewById(R.id.updatePanel);
            debt = itemView.findViewById(R.id.debtSubs);

        }
    }

    //update debt method
public void update(int id , int debt){

    final String debtorDebt = Integer.toString(debt).trim();
    final String debtorId = Integer.toString(id).trim();

    //  final int debitC = Integer.parseInt(debit);
    //int store_id= Integer.parseInt(storeId.getText().toString().trim());

    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Constants.URL_DEBT_UPDATE,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject= new JSONObject(response);
                        StyleableToast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG,R.style.ToastSuccess).show();
//                        Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
            //            Toast.makeText(context,error.getMessage(),Toast.LENGTH_LONG).show();
        }
    }){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params = new HashMap<>();
            params.put("id",debtorId);
            params.put("price",debtorDebt);

            return params;
        }
    };

    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
}

//delete debt method
public void delete(int id){
    final String url = Constants.ipAddress+"/android/v1/DeleteDebt.php?id="+id;

    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                JSONObject jsonObject= (JSONObject) new JSONObject(response);
                StyleableToast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG,R.style.ToastSuccess).show();
//                Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
            //            Toast.makeText(context,error.getMessage(),Toast.LENGTH_SHORT).show();

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
                       // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        //Send the request


                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                Constants.URL_Add_TOKEN,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject jsonObject= new JSONObject(response);
                                       //     Toast.makeText(context,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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

    private  void sendNotification(String title2 , String content,String product){
        final String fullname = SharedPrefManager.getInstance(context).getAdminFirstName() +" "+SharedPrefManager.getInstance(context).getAdminLastName();
        final String message = fullname+" "+content+" "+product;
        final String title = title2;
        final  String store = SharedPrefManager.getInstance(context).getStoreId()+"";
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
