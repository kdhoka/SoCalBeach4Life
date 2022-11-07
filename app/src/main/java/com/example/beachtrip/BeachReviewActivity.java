package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BeachReviewActivity extends AppCompatActivity {
    private String beachID;
    private FirebaseDatabase root;
    private DatabaseReference Reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_review);
        Intent intent = getIntent();
        beachID = intent.getStringExtra("id");

        root = FirebaseDatabase.getInstance();
        Reviews = root.getReference("Reviews");

        displayReview();
    }

    private void displayReview() {

    }

    public void onClickBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}