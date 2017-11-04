package com.auribises.volleydemo;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    EditText eTxtName, eTxtPhone, eTxtEmail;
    Button btnRegister;

    String name, phone, email;

    StringRequest stringRequest;
    RequestQueue requestQueue;

    boolean updateMode;

    User user;

    String urlInsert = "http://www.auribises.com/volley/registeruser.php";
    String urlUpdate = "http://www.auribises.com/volley/updateuser.php";

    boolean checkInternetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnected();
    }

    void initViews(){
        eTxtName = (EditText)findViewById(R.id.editTextName);
        eTxtPhone = (EditText)findViewById(R.id.editTextPhone);
        eTxtEmail = (EditText)findViewById(R.id.editTextEmail);

        btnRegister = (Button)findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(this);

        Intent rcv = getIntent();
        updateMode = rcv.hasExtra("keyUser");

        if(updateMode){
            user = (User)rcv.getSerializableExtra("keyUser");
            eTxtName.setText(user.name);
            eTxtPhone.setText(user.phone);
            eTxtEmail.setText(user.email);

            btnRegister.setText("Update User");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        initViews();

        requestQueue = Volley.newRequestQueue(this);
    }

    // RegisterUser shall send the data to web server using Volley
    void registerUser(){
        //1. Method
        //2. URL
        //3. ResponseListener (Handles Response from Server)
        //4. ErrorListener (Handles Error from Server)

        String url = "";
        if(updateMode)
            url = urlUpdate;
        else
            url = urlInsert;

        stringRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");
                            String message = jsonObject.getString("message");

                            Toast.makeText(RegisterUserActivity.this,message,Toast.LENGTH_LONG).show();

                            if(updateMode)
                                finish();

                            eTxtName.setText("");
                            eTxtPhone.setText("");
                            eTxtEmail.setText("");

                        }catch (Exception e){
                            Toast.makeText(RegisterUserActivity.this,"Some Exception: "+e,Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterUserActivity.this,"Some Error: "+error,Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String,String> map = new HashMap<String, String>();

                if(updateMode)
                    map.put("id",String.valueOf(user.uid));

                map.put("name",name);
                map.put("phone",phone);
                map.put("email",email);

                return map;
            }
        }
        ;

        requestQueue.add(stringRequest); // Execution of REST


    }

    @Override
    public void onClick(View view) {
        name = eTxtName.getText().toString().trim();
        phone = eTxtPhone.getText().toString().trim();
        email = eTxtEmail.getText().toString().trim();

        if(checkInternetConnectivity()){
            registerUser();
        }else{
            // Replace Toast with Dialog
            Toast.makeText(this,"Please Connect to Internet and Try Again!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0,101,0,"All Users");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == 101) {
            Intent intent = new Intent(RegisterUserActivity.this, AllUsersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
