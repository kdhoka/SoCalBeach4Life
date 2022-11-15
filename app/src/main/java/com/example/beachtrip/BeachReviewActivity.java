package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BeachReviewActivity extends AppCompatActivity {
    private String beachID;
    private FirebaseDatabase root;
    private DatabaseReference reviewRef;
    private ValueEventListener reviewCredentialListener;
    private ValueEventListener beachCredentialListener;
    private DatabaseReference beachRef;

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
        reviewRef = root.getReference("Reviews"); //pointer to the Review tree

        reviewCredentialListener = new ValueEventListener() {
            private static final String TAG = "Review read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                beach_reviews = new ArrayList<Review>();
                totalRate = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String beachKey = dsp.child("beach").getValue().toString();
                    if (beachKey.equals(beachID)) {

                        String content = dsp.child("content").getValue().toString();
                        double rate = Double.parseDouble(dsp.child("rate").getValue().toString());
                        boolean isAnonymous = (boolean) dsp.child("isAnonymous").getValue();
                        String username = dsp.child("uID").getValue().toString();
                        String reviewId = dsp.getKey().toString();
                        String imageURL = dsp.child("image").getValue().toString();
                        String imagePath = dsp.child("imagePath").getValue().toString();
                        Review r = new Review(reviewId, username, beachKey, isAnonymous, rate, content, imageURL, imagePath);

                        beach_reviews.add(r);
                        totalRate += rate;
                    }
                }
                onFinishLoading();
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
        //dynamically get beach name from beach DB tree, no other way.
        if (reviewCredentialListener != null){
            reviewRef.removeEventListener(reviewCredentialListener);
            beachRef = root.getReference("beaches");
        }

        ValueEventListener beachCredentialListener = new ValueEventListener() {
            private static final String TAG = "Beach read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id = dsp.getKey().toString();
                    if(id.equals(beachID)){
                        String beachName = dsp.child("name").getValue().toString();
                        TextView review_title = findViewById(R.id.beach_review_title);
                        review_title.setText(beachName);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        beachRef.addValueEventListener(beachCredentialListener);

        if(this.beach_reviews.size() == 0){
            return;
        }
        else{
            index = 0;
            displayReview();
        }
    }

    private void displayReview() {
        if (beachCredentialListener != null){
            beachRef.removeEventListener(beachCredentialListener);
        }

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

        ImageView iv = findViewById(R.id.image);
        if(!r.getImageURL().equals("nullURL")){
            iv.setVisibility(View.VISIBLE);
            String link = r.getImageURL();
            ProgressBar progressBar = findViewById(R.id.beach_review_progressBar);
            progressBar.setVisibility(View.VISIBLE);
            // Hide progress bar on successful load
            Picasso.get().load(link)
                    .into(iv, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            System.out.println("Picasso load image failed");
                        }
                    });
        }
        else{
            iv.setVisibility(View.INVISIBLE);
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
                String name = dataSnapshot.child(r.getUser_ID()).child("name").getValue().toString();
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
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(this, UserReviewPage.class);
            intent.putExtra("beachID", beachID);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please sign in before proceeding!", Toast.LENGTH_SHORT).show();
        }
    }
}