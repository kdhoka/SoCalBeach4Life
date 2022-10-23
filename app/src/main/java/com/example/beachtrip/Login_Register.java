package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

public class Login_Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        Intent intent = getIntent();
        DataRetrieval DR = DataRetrieval.getInstance();
        User user = DR.getUserCredential();
        TextView credentialPrint = findViewById(R.id.credential);
        if (user == null){
            credentialPrint.setText("no user1");//hard coded to get data of user1 only
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("name: ").append(user.getName()).append("\n");
            sb.append("email: ").append(user.getEmail()).append("\n");
            sb.append("password: ").append(user.getPassword()).append("\n");
            sb.append("reviews: ");
            Review[] reviews = user.getReviews();
            for (int i = 0; i < reviews.length; i++){
                sb.append(reviews[i]).append("\n");
            }
            credentialPrint.setText(sb.toString());
        }
    }



}