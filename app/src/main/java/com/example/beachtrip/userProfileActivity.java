package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class userProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth;
    private FirebaseUser currUser;
    private String beachID;
    private TextView my_review_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        TextView name_tv = findViewById(R.id.name_tv);
        setText();

        Spinner spinner = findViewById(R.id.beachChoice);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.beaches, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        my_review_btn = findViewById(R.id.button2);
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
        intent.putExtra("beachID", beachID);//TODO: make it dynamic on run time
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        my_review_btn.setVisibility(View.VISIBLE);

        //get BeachID
        if (text.equals("Marina Beach")) {
            beachID = "beach1";
        }else if (text.equals("Venice Beach")) {
            beachID = "beach2";
        }else if(text.equals("Santa Monica State Beach")) {
            beachID = "beach3";
        }else if (text.equals("Manhattan Beach")) {
            beachID = "beach4";
        } else if (text.equals("Cabrillo Beach")) {
            beachID = "beach5";
        }else if (text.equals("Alamitos Beach")) {
            beachID = "beach6";
        } else {
            beachID = "beach0";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        my_review_btn = findViewById(R.id.button2);
        my_review_btn.setVisibility(View.INVISIBLE);
    }
}