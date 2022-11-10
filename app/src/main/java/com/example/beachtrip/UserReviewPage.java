package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserReviewPage extends AppCompatActivity {
    FirebaseDatabase root;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String beachID;
    String userID;
    Boolean isAnon = false;
    Review review = null;
    String image;
    private static final String TAG = "Create Review.";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_review);

        beachID = getIntent().getStringExtra("beachID");
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        image = getIntent().getStringExtra("image");
        Button b = findViewById(R.id.imageUpload);
        if(image != null){
            System.out.println(image);
        }

        root = FirebaseDatabase.getInstance();
        DatabaseReference beachRef = root.getReference("beaches");
        ValueEventListener beachCredentialListener = new ValueEventListener() {
            private static final String TAG = "Beach read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                int size = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id = dsp.getKey().toString();
                    if(id.equals(beachID)){
                        String beachName = dsp.child("name").getValue().toString();
                        TextView beachName_tv = findViewById(R.id.beach_name_val);
                        beachName_tv.setText(beachName);
                        loadRestContent();
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
    }

    private void loadRestContent() {
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
                        Double rate = Double.parseDouble(dsp.child("rate").getValue().toString());
                        Boolean isAnonymous = (Boolean)dsp.child("isAnonymous").getValue();
                        //String imageLink = dsp.child("image").getValue().toString();
                        review = new Review(reviewID, userKey, beachKey, isAnonymous, rate, content);
                        break;
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
        String rating = "";
        String isAnonStr = "false";
        isAnon = false;
        String content = "";
        //String imageLink = "";

        TextView rating_view = findViewById(R.id.rating);
        TextView isAnon_btn = findViewById(R.id.anon_btn);
        TextView content_view = findViewById(R.id.content_tv);
        TextView delete_btn = findViewById(R.id.delete);
        if (review != null){
            rating = String.valueOf(review.getRating());
            isAnon = review.getIs_anonymous();
            if (isAnon){
                isAnonStr = "true";
            }
            content = review.getContent();
            delete_btn.setVisibility(View.VISIBLE);
        } else {
            delete_btn.setVisibility(View.INVISIBLE);
        }

        if (!rating.isEmpty()){//only load rating is there is one, else display hint
            rating_view.setText(rating);
        }
        isAnon_btn.setText(isAnonStr);
        if(!content.isEmpty()){//only load content is there is one, else display hint
            content_view.setText(content);
        }

    }


    public void onClickBackFromMyReview(View view) {
        //TODO: clear all text fields
        Intent intent = new Intent(this, BeachInfoActivity.class);
        intent.putExtra("id", beachID);
        startActivity(intent);
    }

    public void onClickConfirm(View view) {
        root = FirebaseDatabase.getInstance();
        DatabaseReference reviewRef = root.getReference("Reviews");
        TextView rating_view = findViewById(R.id.rating);
        TextView isAnon_btn = findViewById(R.id.anon_btn);
        TextView content_view = findViewById(R.id.content_tv);

        String rating = rating_view.getText().toString();
        String content = content_view.getText().toString();
        String imageLink = "";
        Double rate_double;

        try {
            //TODO: check for valid rating input.
            rate_double = Double.parseDouble(rating);

            if (!(0 <= rate_double) && (rate_double <= 5)) {
                rating_view.setError("Rating must be a double in range 0-5");
                return;
            }
        } catch (NullPointerException e){
            //catch empty rating string input error
            rating_view.setError("Rating cannot be empty!");
            throw e;
        } catch (NumberFormatException e){
            rating_view.setError("Rating must be a number!");
            throw e;
        }

        if (review == null){
            //create a new review obj, assign its data field input text, and push it to database
            ValueEventListener newReviewListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d(TAG, "onClick database listener activated");
                        // Get value of each attribute of a User ob
                        long reviewIndex = snapshot.getChildrenCount() + 1;
                        String reviewID = "Review" + reviewIndex;

                        reviewRef.child(reviewID).push();
                        DatabaseReference newReview = reviewRef.child(reviewID);

                        newReview.child("beach").push();
                        newReview.child("beach").setValue(beachID);

                        newReview.child("uID").push();
                        newReview.child("uID").setValue(userID);

                        newReview.child("rate").push();
                        newReview.child("rate").setValue(rating);

                        newReview.child("content").push();
                        newReview.child("content").setValue(content);

                        newReview.child("isAnonymous").push();
                        newReview.child("isAnonymous").setValue(isAnon);
                        Log.d(TAG, "onClick database listener finished.");
                        Toast.makeText(UserReviewPage.this, "Review updated!", Toast.LENGTH_SHORT).show();
                        //TODO: add after image upload feature is fully functional
//                    newReview.child("image").push();
//                    newReview.child("image").setValue(imageLink);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d(TAG, "push new review to DB failed.");
                }
            };
            reviewRef.addValueEventListener(newReviewListener);
        } else {
            //else update existing review's data in DB.
            String reviewID = review.getReviewID();
            DatabaseReference currReview = reviewRef.child(review.getReviewID());
            currReview.child("rate").setValue(rating);
            currReview.child("content").setValue(content);
            currReview.child("isAnonymous").setValue(isAnon);
        }
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
        /*get a reference on the node to be deleted
        *implement a single value event listener
        *add the listener to the reference
        *in onDataChange, call removeValue() on the ref of the node*/
        DatabaseReference reviewRef = root.getReference("Reviews");
        Query query = reviewRef.child(review.getReviewID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("delete failed");
            }
        });

        //set text field to be empty on display
    }

    public void onClickImageUpload(View view){
        Intent intent = new Intent(this, imageUploadPage.class);
        intent.putExtra("beachID", beachID);
        startActivity(intent);
    }
}