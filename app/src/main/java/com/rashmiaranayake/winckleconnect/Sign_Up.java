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
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;


public class Sign_Up extends AppCompatActivity {
    EditText Username,Password;
    Button SignUp_Button;
    String username,email, password;
    TextView next_login;
    private FirebaseAuth firebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign__up);


        Username = (EditText) findViewById(R.id.text_username);
        Password = (EditText) findViewById(R.id.text_password);
        SignUp_Button = (Button) findViewById(R.id.SignUp_Button);
        next_login = (TextView) findViewById(R.id.Next_LogIn);

        Firebase.setAndroidContext(this);

        //when user already has an account and needs to log in
       next_login.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Sign_Up.this,Log_In.class));
           }
       });


        //when user needs to create an account
    SignUp_Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            username = Username.getText().toString();
            //email = Email.getText().toString();
            password = Password.getText().toString();

            //showing warnings when username or password empty
            if(username.equals("")){
                Username.setError("Can Not Be Empty");
            }
           // else if(email.equals("")){
             //   Email.setError("Can Not Be Empty");
            //}
            else if(password.equals("")){
                Password.setError("Can Not Be Empty");
            }

            else if(Username.length()<4){
                Username.setError("at least 4 characters long");
                }
            else if(password.length()<6){
                Password.setError("At least 6 characters long");
            }
            else {

               final ProgressDialog progressDialog = new ProgressDialog(Sign_Up.this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();



                startActivity(new Intent(Sign_Up.this, Contacts.class));
                String url = "https://collegareoriginal.firebaseio.com/users.json";




                //save password and user name in firebase
            StringRequest stringrequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {




                @Override
                public void onResponse(String response) {
                    Firebase reference = new Firebase("https://collegareoriginal.firebaseio.com/users");

                    //set password as child in firebase
                    if (response.equals("null")) {

                        reference.child(username).child("password").setValue(password);;
                        Toast.makeText(Sign_Up.this, "Sign In Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Sign_Up.this, Contacts.class));
                    } else {
                        try {
                            JSONObject obj = new JSONObject(response);

                            if (!obj.has(username)) {


                                // when user successfully registered go to contact page
                                reference.child(username).child("password").setValue(password);;
                                Toast.makeText(Sign_Up.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Sign_Up.this, Contacts.class));
                            } else {

                                //when existing user try to sign up instant of login goes to login page
                                Toast.makeText(Sign_Up.this, "Username Already Exists & Log In", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(Sign_Up.this, Log_In.class));
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
                    System.out.println("" + error );
                    progressDialog.dismiss();
                }
            });

                RequestQueue rQueue = Volley.newRequestQueue(Sign_Up.this);
                rQueue.add(stringrequest);
            }
        }
    });


            }

    }
