package com.auribises.volleydemo;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllUsersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    StringRequest stringRequest;
    RequestQueue requestQueue;

    String url = "http://www.auribises.com/volley/retrieveusers.php";
    String urlDelete = "http://www.auribises.com/volley/deleteuser.php";

    ArrayList<User> userList;
    ArrayList<String> userNameList;

    ArrayAdapter<String> adapter;

    ListView listView;

    User user;
    int pos;

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
                                listView.setOnItemClickListener(AllUsersActivity.this);
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

    void deleteUser(){
        stringRequest = new StringRequest(Request.Method.POST, urlDelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    int success = jsonObject.getInt("success");

                    if(success == 1){
                        Toast.makeText(AllUsersActivity.this,user.name+" Deleted !!",Toast.LENGTH_LONG).show();
                        userNameList.remove(pos);
                        userList.remove(pos);
                        adapter.notifyDataSetChanged();
                    }

                }catch (Exception e){
                    Toast.makeText(AllUsersActivity.this,"Some Exception: "+e,Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllUsersActivity.this,"Some Error: "+error,Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("id",String.valueOf(user.uid));
                return map;
            }
        }
        ;

        requestQueue.add(stringRequest);
    }

    void askForDeletion(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+user.name);
        builder.setMessage("Are you Sure ?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUser();
            }
        });

        builder.setNegativeButton("Cancel",null);

        builder.create().show();
    }

    void showOptions(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String items[] = new String[]{"Update", "Delete", "View"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        Intent intent = new Intent(AllUsersActivity.this,RegisterUserActivity.class);
                        intent.putExtra("keyUser",user);
                        startActivity(intent);
                        break;

                    case 1:
                        askForDeletion();
                        break;

                    case 2:

                        break;
                }
            }
        });
        builder.create().show();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        pos = i;
        user = userList.get(i);
        showOptions();

    }
}
