package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserReviewPage extends AppCompatActivity {
    FirebaseDatabase root;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String beachID;
    String userID;
    Boolean isAnon = false;
    Review review = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        
        root = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef = root.getReference("Reviews"); //pointer to the Review tree

        ValueEventListener reviewCredentialListener = new ValueEventListener() {
            private static final String TAG = "Review read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String beachKey = dsp.child("beach").getValue().toString();
                    String userKey = dsp.child("uID").getValue().toString();
                    //if found a review for selected beach by current user, display it.
                    if (beachKey.equals(beachID) && userKey.equals(userID)) {
                        String reviewID = dsp.getKey().toString();//used to identify if this is a new review
                        String content = dsp.child("content").getValue().toString();
                        Double rate = (double)dsp.child("rate").getValue();
                        Boolean isAnonymous = (Boolean)dsp.child("isAnonymous").getValue();
                        String imageLink = dsp.child("image").getValue().toString();
                        review = new Review(reviewID, userKey, beachKey, isAnonymous, rate, content);
                    }
                }
                onFinishLoading();
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){
                DatabaseError databaseError = null;
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };

        reviewRef.addValueEventListener(reviewCredentialListener);
    }

    private void onFinishLoading() {
        String beachName = "Beach to be reviewed";
        String rating = "Enter rating 0-5";
        String isAnonStr = "false";
        isAnon = false;
        String content = "No existing review content for this beach by you...";
        String imageLink = "";

        TextView beachName_view = findViewById(R.id.beach_name_val);
        TextView rating_view = findViewById(R.id.rating);
        TextView isAnon_btn = findViewById(R.id.anon_btn);
        TextView content_view = findViewById(R.id.content_tv);
        TextView delete_btn = findViewById(R.id.delete);
        if (review != null){
            beachName = review.getBeach_name();
            rating = String.valueOf(review.getRating());
            isAnon = review.getIs_anonymous();
            if (isAnon){
                isAnonStr = "true";
            }
            content = review.getContent();
            delete_btn.setVisibility(View.VISIBLE);}
//        } else {
//            delete_btn.setVisibility(View.INVISIBLE);
//        }

        beachName_view.setText(beachName);
        rating_view.setText(rating);
        isAnon_btn.setText(isAnonStr);
        content_view.setText(content);
    }


    public void onClickBackFromMyReview(View view) {
        Intent intent = new Intent(this, BeachReviewActivity.class);
        intent.putExtra("id", beachID);
        startActivity(intent);
    }

    public void onClickConfirm(View view) {
    }


    public void onClickAnon(View view) {
        TextView isAnonymous_tv = findViewById(R.id.anon_btn);
        String isAnonStr = isAnonymous_tv.getText().toString();
        if (isAnonStr.equals("true")){
            //change button text and state to false;
            isAnonymous_tv.setText("false");
            isAnon = false;
        } else {
            isAnonymous_tv.setText("true");
            isAnon = true;
        }
    }

    public void onClickDelete(View view) {
    }

//    DatabaseReference Beaches = root.getReference("Beaches");
//
//    ValueEventListener beachCredentialListener = new ValueEventListener() {
//        private static final String TAG = "Beach read.";
//
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            // Get value of each attribute of a User ob
//            int size = 0;
//            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                String id = dsp.getKey().toString();
//                if (id.equals(beachID)) {
//                    TextView title_tv = findViewById(R.id.beachReviewTitle);
//                    title_tv.setText(dsp.child("name").getValue().toString());
//                    break;
//                }
//            }
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//
//    };
//        Beaches.addValueEventListener(beachCredentialListener);
}