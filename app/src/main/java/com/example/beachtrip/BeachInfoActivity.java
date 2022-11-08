package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BeachInfoActivity extends AppCompatActivity {
    private String beachID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beach_info);
        Intent intent = getIntent();
        beachID = intent.getStringExtra("id");
        FirebaseDatabase root = FirebaseDatabase.getInstance();
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
                      TextView nV = findViewById(R.id.nameFieldView);
                      TextView lV = findViewById(R.id.locationFieldView);
                      TextView hV = findViewById(R.id.hoursFieldView);
                      nV.setText(dsp.child("name").getValue().toString());
                      hV.setText(dsp.child("hours").getValue().toString());
                      String xpos = dsp.child("xpos").getValue().toString();
                      String ypos =dsp.child("ypos").getValue().toString();
                      String location = ""+xpos.substring(0,xpos.indexOf('.')+3)+", "+ypos.substring(0,ypos.indexOf('.')+3);
                      lV.setText(location);
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

    public void onClickReviews(View view) {
        Intent intent = new Intent(this, BeachReviewActivity.class);
        intent.putExtra("id", beachID);
        startActivity(intent);
    }
}