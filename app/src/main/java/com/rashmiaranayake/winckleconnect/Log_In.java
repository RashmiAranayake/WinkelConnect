package com.rashmiaranayake.winckleconnect;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Log_In extends AppCompatActivity {


    EditText Text_username, Text_password;
    Button Button_Login;
    String user, password;
    TextView next_signup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);


        Text_username = (EditText) findViewById(R.id.Text_username);
        Text_password = (EditText) findViewById(R.id.Text_password);
        Button_Login= (Button) findViewById(R.id.login_Button);
        next_signup=(TextView)findViewById(R.id.Next_SignIn);




        //new user and need to sign up
        next_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Log_In.this, Sign_Up.class));
            }
        });

        Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = Text_username.getText().toString();
                password = Text_password.getText().toString();

                if (user.equals("")) {
                    Text_username.setError("Can Not Be Empty");
                } else if (Text_password.equals("")) {
                    Text_password.setError("Can Not Be Empty");
                } else {
                    String url = "https://collegareoriginal.firebaseio.com/users.json";
                    final ProgressDialog progressDialog = new ProgressDialog(Log_In.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.show();


                    //getting information from firebase
                   StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                       @Override
                       public void onResponse(String response) {
                           if (response.equals("null")) {
                               Toast.makeText(Log_In.this, "User Not Found,Please Sign In", Toast.LENGTH_LONG).show();
                           } else {
                               try {
                                   JSONObject obj = new JSONObject(response);

                                   if (!obj.has(user)) {
                                       //checking user availability
                                       Toast.makeText(Log_In.this, "User Not Found, If You Want To Go Further Sign In", Toast.LENGTH_LONG).show();
                                   } else if (obj.getJSONObject(user).getString("password").equals(password)) {

                                       // if user exist
                                       ContactsDetails.username = user;
                                       ContactsDetails.password = password;
                                       startActivity(new Intent(Log_In.this, Contacts.class));
                                   } else {
                                       //if user exist and password incorrect
                                       Toast.makeText(Log_In.this, "Incorrect Password", Toast.LENGTH_LONG).show();
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }

                           progressDialog.dismiss();
                       }
                   }, new Response.ErrorListener() {
                       @Override
                       public void onErrorResponse(VolleyError error) {
                           System.out.println("" + error);
                           progressDialog.dismiss();
                       }
                   });

                    //setting up custom behaviour
                    RequestQueue rQueue = Volley.newRequestQueue(Log_In.this);
                    rQueue.add(request);
                }

            }
        });
    }
}