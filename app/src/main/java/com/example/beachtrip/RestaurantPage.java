package com.example.beachtrip;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_info);
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference restRef= root.getReference("Restaurants"); //pointer to the Restaurant tree

        ArrayList<String> restaurantNames = new ArrayList<String>();

        ValueEventListener restaurantCredentialListener = new ValueEventListener() {
            private static final String TAG = "Restaurant read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    //System.out.println("here");
                    //String userkey  = dsp.getKey();
                    String restName = dsp.child("name").getValue().toString();
                    restaurantNames.add(restName);
                }
                TextView tv = findViewById(R.id.restaurant1);
                tv.setText(restaurantNames.get(1));
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        restRef.addValueEventListener(restaurantCredentialListener);

        //tv.setText(rests.get(0).getName());

        /*
        for(Restaurant rest : rests){
            //TODO
        }*/
    }

    public void onReturn(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
