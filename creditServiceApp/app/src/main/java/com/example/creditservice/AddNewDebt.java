package com.example.creditservice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.muddzdev.styleabletoast.StyleableToast;
import com.tapadoo.alerter.Alerter;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddNewDebt extends AppCompatActivity  implements View.OnClickListener {

    private Button buttonBack , buttonUpload,buttonSubmit;
    private EditText productTitle,productDebt;
    private TextView debtorName;
    private ImageView productImage ;

    private ProgressDialog progressDialog;

    private static final int STORAGE_PERMISSION_CODE=5005;
    private static final int PICK_IMAGE_REQUEST = 25;

    private Uri filepath ;
    private Bitmap bitmap;


    private static final String upload_url = Constants.ipAddress+"/android/v1/AddNewDebt.php";

    Debtor debtorI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_debt);

        requestStoragePermission();

        buttonBack = findViewById(R.id.buttonReturn2);
        buttonUpload = findViewById(R.id.uploadImage);
        buttonSubmit = findViewById(R.id.addNewDebtButton);

        productTitle = findViewById(R.id.productName);
        productDebt = findViewById(R.id.productDebtC);

        debtorName = findViewById(R.id.debtorName);

        productImage = findViewById(R.id.productImageView);

        progressDialog = new ProgressDialog(this);


        productDebt.addTextChangedListener(textWatcher);
        productTitle.addTextChangedListener(textWatcher);





        Intent i = getIntent();
        final Debtor debtor = (Debtor) i.getSerializableExtra("debtor");
         debtorI = debtor;
        //initialise debtor name -->top
        if (debtor != null){
            debtorName.setText(debtor.getFirstName()+" "+debtor.getLastName());
        }

        //Use buttons
        buttonBack.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,ProfileActivity.class));
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
    private  void requestStoragePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                StyleableToast.makeText(this,"Permission granted",Toast.LENGTH_LONG,R.style.ToastSuccess).show();
               // Toast.makeText(this,"Permission granted ",Toast.LENGTH_LONG).show();
            }else {
                StyleableToast.makeText(this,"Permission not granted",Toast.LENGTH_LONG,R.style.ToastFailed).show();

                //   Toast.makeText(this,"Permission not granted ",Toast.LENGTH_LONG).show();

            }
        }
    }
//Show the image in image view
    private void showFile(){
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,"Select picture"),PICK_IMAGE_REQUEST);
        //startActivityForResult(Intent.createChooser(intent,"Select picture"), PICK );
   // Toast.makeText(this,"Method show file good",Toast.LENGTH_LONG).show();
    }

    //Method to handle the choosing image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   Toast.makeText(this,"on activity result before ifff"+data+",2"+resultCode+",3"+requestCode,Toast.LENGTH_LONG).show();

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            filepath = data.getData();
       //     Toast.makeText(this,"on activity result before tryyyyy"+filepath,Toast.LENGTH_LONG).show();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                productImage.setImageBitmap(bitmap);
             //   Toast.makeText(this,"method onaactivity result works",Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getPath (Uri uri){
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);

        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null,MediaStore.Images.Media._ID+"= ?",new String[]{document_id},null
        );
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        return path;
    }

    //-------------------------- > Send Token to Server

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
                     //   Toast.makeText(AddNewDebt.this, msg, Toast.LENGTH_SHORT).show();

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
                                  //          Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
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
                                //    Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String> params = new HashMap<>();
                                params.put("token",mytoken);

                                return params;
                            }
                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(AddNewDebt.this);
                        requestQueue.add(stringRequest);
                    }
                });
        //  final int debitC = Integer.parseInt(debit);
        //int store_id= Integer.parseInt(storeId.getText().toString().trim());


    }

    //add new Debt method

    private void uploadImage(){
        String title = productTitle.getText().toString().trim();
        String debt = productDebt.getText().toString().trim();
        String path = getPath(filepath);

        try {
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(this,uploadId,upload_url)
                    .addFileToUpload(path,"product_picture")
                    .addParameter("product_name",title).setUtf8Charset()
                    .addParameter("price",debt)
                    .addParameter("debtor_id",""+debtorI.getId())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(3)
                    .startUpload();

        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    //send the notification

    private  void sendNotification(){
        final String fullname = SharedPrefManager.getInstance(this).getAdminFirstName() +" "+SharedPrefManager.getInstance(this).getAdminLastName();
        final String message = fullname+" أضاف دينا جديدا للزبون : "+debtorI.getFirstName()+" "+debtorI.getLastName();
        final String title = "إضافة دين جديد";
        final String store = SharedPrefManager.getInstance(this).getStoreId()+"";
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
                  StyleableToast.makeText(getApplicationContext(),"تمت الاضافة بنجاح",Toast.LENGTH_LONG,R.style.ToastSuccess).show();
//                          /**/  Toast.makeText(getApplicationContext(),
//                                    "تمت الاضافة بنجاح",Toast.LENGTH_LONG).show();
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
               // Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
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
        if(v==buttonBack){
            Intent i = new Intent(this,DebtorDetails.class);
            i.putExtra("myObj",debtorI);
            v.getContext().startActivity(i);
          //  overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
       //     startActivity(new Intent(this,DebtorDetails.class));

        }
        if(v==buttonUpload){
                showFile();
        }
        if(v==buttonSubmit){
            saveToken();
            uploadImage();
            sendNotification();
        }

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
String productName = productTitle.getText().toString().trim();
String productPrice = productDebt.getText().toString().trim();

buttonSubmit.setEnabled(!productName.isEmpty() && !productPrice.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
