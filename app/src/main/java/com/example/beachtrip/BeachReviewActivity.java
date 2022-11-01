package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BeachReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_review);
        Intent intent = getIntent();
        String beach = intent.getStringExtra("beach");
        displayReview(beach);
    }

    private void displayReview(String beach) {
    }

    public void onClickBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}