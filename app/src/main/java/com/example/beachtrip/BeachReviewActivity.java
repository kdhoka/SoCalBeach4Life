package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BeachReviewActivity extends AppCompatActivity {
    private String beachID;
    private FirebaseDatabase root;
    private DatabaseReference Reviews;
    private ArrayList<Review> beach_reviews;
    int index;
    private String curUsername;
    private double totalRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_review);

        Intent intent = getIntent();
        beachID = intent.getStringExtra("id");

        root = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef= root.getReference("Reviews"); //pointer to the Review tree

        ValueEventListener reviewCredentialListener = new ValueEventListener() {
            private static final String TAG = "Review read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                beach_reviews = new ArrayList<Review>();
                totalRate = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String beachKey = dsp.child("beach").getValue().toString();
                    if(beachKey.equals(beachID)){
                        String content = dsp.child("content").getValue().toString();
                        double rate = (double) dsp.child("rate").getValue();
                        totalRate += rate;
                        boolean isAnonymous = (boolean) dsp.child("isAnonymous").getValue();
                        String username = dsp.child("uID").getValue().toString();
                        Review r = new Review(username, beachKey, isAnonymous, rate, content);
                        beach_reviews.add(r);
                    }
                }
                onFinishLoading();
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        reviewRef.addValueEventListener(reviewCredentialListener);
    }

    private void onFinishLoading(){
        if(this.beach_reviews.size() == 0){
            return;
        }
        else{
            index = 0;
            displayReview();
        }
    }

    private void displayReview() {
        TextView contentTv = findViewById(R.id.review);
        TextView nameTv = findViewById(R.id.username);
        Review r = beach_reviews.get(index);
        contentTv.setText(r.getContent());
        TextView rateTv = findViewById(R.id.rating);
        rateTv.setText(String.valueOf(r.getRating()) + " stars");
        if(!r.getIs_anonymous()){
            displayUsername();
        }
        else{
            nameTv.setText("Anonymous review");
        }
        TextView average = findViewById(R.id.averageRating);
        int average1 = (int) (totalRate * 10 / beach_reviews.size());
        average.setText("average rate: " + String.valueOf(((double) average1) / 10.0));
    }

    private void displayUsername(){
        root = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef= root.getReference("Users"); //pointer to the Review tree

        ValueEventListener reviewCredentialListener = new ValueEventListener() {
            private static final String TAG = "User read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                Review r = beach_reviews.get(index);
                String name = dataSnapshot.child(r.getUser_name()).child("name").getValue().toString();
                TextView tv = findViewById(R.id.username);
                tv.setText(name + "'s review");
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        reviewRef.addValueEventListener(reviewCredentialListener);
    }

    public void onClickNextReview(View view){
        if(index < beach_reviews.size() - 1){
            index++;
            displayReview();
        }
    }

    public void onClickPrevReview(View view){
        if(index > 0){
            index--;
            displayReview();
        }
    }

    public void onClickBack(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onMyReview(View view){
        Intent intent = new Intent(this, UserReviewPage.class);
        startActivity(intent);
    }
}