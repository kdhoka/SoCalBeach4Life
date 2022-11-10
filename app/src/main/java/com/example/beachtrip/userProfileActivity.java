package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView name_tv = findViewById(R.id.name_tv);
        setText();
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void setText(){
        mAuth = FirebaseAuth.getInstance();
        String uID = mAuth.getCurrentUser().getUid();

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference Users= root.getReference("Users");


        ValueEventListener credentialListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name;
                String email;
                String pwd;

                name = snapshot.child(uID).child("name").getValue().toString();
                email = snapshot.child(uID).child("email").getValue().toString();
                pwd = snapshot.child(uID).child("password").getValue().toString();

                TextView name_tv = findViewById(R.id.name_tv);
                TextView email_tv = findViewById(R.id.email_tv);
                TextView pwd_tv = findViewById(R.id.pwd_tv);

                name_tv.setText(name);
                email_tv.setText(email);
                pwd_tv.setText(pwd);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        Users.addValueEventListener(credentialListener);
    }

    public void onClickProfileToMyReview(View view) {
        Intent intent = new Intent(this, UserReviewPage.class);
        intent.putExtra("beachID", "beach4");//TODO: make it dynamic on run time
        startActivity(intent);
    }
}