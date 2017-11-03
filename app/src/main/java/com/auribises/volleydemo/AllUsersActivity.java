package com.auribises.volleydemo;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String url = "http://www.auribises.com/volley/retrieveusers.php";

    ArrayList<User> userList;
    ArrayList<String> userNameList;

    ArrayAdapter<String> adapter;

    ListView listView;

    boolean checkInternetConnectivity(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo!=null && networkInfo.isConnected();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        requestQueue = Volley.newRequestQueue(this);
        listView = (ListView)findViewById(R.id.listView);

        if(checkInternetConnectivity())
            retrieveUsers();
        else
            Toast.makeText(this,"Please Check Internet Connectivity and try Again",Toast.LENGTH_LONG).show();


    }


    void retrieveUsers(){

        stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{

                            JSONObject jsonObject = new JSONObject(response);
                            int success = jsonObject.getInt("success");

                            int uid=0;
                            String n="",p="",e="";
                            if(success == 1){
                                userList = new ArrayList<>();
                                userNameList = new ArrayList<>();

                                JSONArray jsonArray = jsonObject.getJSONArray("users");
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject jObj = jsonArray.getJSONObject(i);

                                    uid = jObj.getInt("uid");
                                    n = jObj.getString("name");
                                    p = jObj.getString("phone");
                                    e = jObj.getString("email");

                                    User user = new User(uid,n,p,e);
                                    userList.add(user);
                                    userNameList.add(n);
                                }

                                adapter = new ArrayAdapter<String>(AllUsersActivity.this,android.R.layout.simple_list_item_1,userNameList);
                                listView.setAdapter(adapter);
                            }else{
                                Toast.makeText(AllUsersActivity.this,"No Records Found",Toast.LENGTH_LONG).show();
                            }

                        }catch (Exception e){
                            Toast.makeText(AllUsersActivity.this,"Some Exception: "+e,Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AllUsersActivity.this,"Some Error: "+error,Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(stringRequest);

    }

    void showOptions(){
        //...
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        User user = userList.get(i);
    }
}
