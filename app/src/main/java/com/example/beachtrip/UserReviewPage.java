package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class UserReviewPage extends AppCompatActivity {
    String beachID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);
        beachID = getIntent().getStringExtra("beachID");
    }


    public void onClickBackFromMyReview(View view) {
        Intent intent = new Intent(this, BeachReviewActivity.class);
        intent.putExtra("id", beachID);
        startActivity(intent);
    }

    public void onClickConfirm(View view) {
    }
}