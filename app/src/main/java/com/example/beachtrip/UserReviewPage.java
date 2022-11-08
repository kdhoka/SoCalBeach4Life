package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserReviewPage extends AppCompatActivity {
    String beachID;
    Boolean isAnon = false;
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