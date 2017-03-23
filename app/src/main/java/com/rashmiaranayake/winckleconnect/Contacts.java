package com.rashmiaranayake.winckleconnect;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;


public class Contacts extends AppCompatActivity implements View.OnClickListener {
    ListView contacts;
    TextView emptycontact;
    Button LogOut;
    ArrayList<String> al = new ArrayList<>();
    int Users = 0;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        contacts = (ListView)findViewById(R.id.Contacts);
        emptycontact = (TextView)findViewById(R.id.EmptyContacts);
        LogOut=(Button)findViewById(R.id.LogOut);

        progressDialog = new ProgressDialog(Contacts.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        LogOut.setOnClickListener(this);
        setTitle("Contacts");

        //database url
        String url = "https://collegareoriginal.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Contacts.this);
        rQueue.add(request);

        contacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactsDetails.chatWith = al.get(position);
                startActivity(new Intent(Contacts.this, Messages.class));
            }
        });
    }
    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();

                if(!key.equals(ContactsDetails.username)) {
                    al.add(key);
                }

                Users++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(Users <=1){
            emptycontact.setVisibility(View.VISIBLE);
            contacts.setVisibility(View.GONE);
        }
        else{
            emptycontact.setVisibility(View.GONE);
            contacts.setVisibility(View.VISIBLE);
            contacts.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(Contacts.this, Log_In.class));
        finish();
    }
}




