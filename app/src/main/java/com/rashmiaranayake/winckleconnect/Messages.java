package com.rashmiaranayake.winckleconnect;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class Messages extends AppCompatActivity {

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;

    @Override
            protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        //back to previous page
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        layout = (LinearLayout) findViewById(R.id.layout1);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);


//displaying chatting with who
        setTitle("You are chatting with "+ContactsDetails.chatWith);





        //set two events in firebase database to save data
        //your details
        reference1 = new Firebase("https://collegareoriginal.firebaseio.com/messages/" + ContactsDetails.username + "_" + ContactsDetails.chatWith);

        //receivers details
        reference2 = new Firebase("https://collegareoriginal.firebaseio.com/messages/" + ContactsDetails.chatWith + "_" + ContactsDetails.username);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
// gettinf text from message area and add them to specific event in fairebase database
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", ContactsDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);

                }

                //clear text area after send button clicked
                messageArea.getText().clear();
            }
        });

        //add listener to user names to check sender and reciver
        reference1.addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(com.firebase.client.DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (userName.equals(ContactsDetails.username)) {
                    addMessageBox("You:-\n" + message, 1);
                } else {
                    addMessageBox(ContactsDetails.chatWith + ":-\n" + message, 2);
                }
            }
            @Override
            public void onChildChanged(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.firebase.client.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.firebase.client.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    //displaying message in two different boxes (sender and receiver)
        public void addMessageBox(String message, int type){
        TextView textView = new TextView(Messages.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);


        if(type == 1) {
            textView.setBackgroundResource(R.drawable.user1);
        }
        else{
            textView.setBackgroundResource(R.drawable.user2);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

        }
    }

