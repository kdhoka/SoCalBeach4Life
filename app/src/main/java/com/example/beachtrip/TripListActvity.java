package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripListActvity extends AppCompatActivity {
    DatabaseReference tripsRef;
    ListView listView;
    ArrayList<String> trip_list = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        setContentView(R.layout.activity_trip_list_actvity);
        listView = (ListView) findViewById(R.id.tripListview);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trip_list);
        listView.setAdapter(arrayAdapter);
        tripsRef = FirebaseDatabase.getInstance().getReference("trips");

        ValueEventListener tripsListener = new ValueEventListener() {
            private static final String TAG = "Trips read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                int size = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    Trip t = dsp.getValue(Trip.class);
                    if(t.getUser().equals(uid)){
                        trip_list.add(t.toString());
                    }
                }
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        tripsRef.addValueEventListener(tripsListener);


    }

}
