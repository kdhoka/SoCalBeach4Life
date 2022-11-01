package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class BeachInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_info);
    }

    public void onClickReviews(View view) {
        Intent intent = new Intent(this, BeachReviewActivity.class);
        intent.putExtra("name", "beach0");
        startActivity(intent);
    }
}