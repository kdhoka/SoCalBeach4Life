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

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.restaurant_info);

        this.name = getIntent().getStringExtra("restaurantName");
        TextView tv = (TextView) findViewById(R.id.restaurant1);
        if(name != null) {
            tv.setText(name);
        }

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference restRef= root.getReference("Restaurants"); //pointer to the Restaurant tree

        ValueEventListener restaurantCredentialListener = new ValueEventListener() {
            private static final String TAG = "Restaurant read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String restName = dsp.child("name").getValue().toString();
                    if(restName.equals(name)){
                        String hours = dsp.child("hours").getValue().toString();
                        TextView hoursTv = findViewById(R.id.hours);
                        System.out.println(hours);
                        hoursTv.setText("Hours: " + hours);
                        break;
                    }
                }
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        restRef.addValueEventListener(restaurantCredentialListener);
    }

    public void onReturn(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
