package com.example.beachtrip;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataRetrieval {
    private static DataRetrieval instance = null;
    public static DataRetrieval getInstance() {
        if (instance == null) {
            instance = new DataRetrieval();
        }
        return instance;
    }
    FirebaseDatabase root = FirebaseDatabase.getInstance();//database
    DatabaseReference reference;//alias of root, pointer to the database
    User credentialsUser;

    public User getUserCredential(){//name, email, password
        reference = root.getReference("User");//pointer to the User tree
        DatabaseReference user1Ref = reference.child("user1");

        ValueEventListener userCredentialListener = new ValueEventListener() {
            private static final String TAG = "Single user credential read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                credentialsUser = dataSnapshot.getValue(User.class);
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        user1Ref.addValueEventListener(userCredentialListener);
        return credentialsUser;
    }

 /*   public boolean isRegistered(String email){
          //TODO
     }
    public ArrayList<User> getUsers(){
        //TODO
    }

    public ArrayList<Beach> getBeachesAndParkingLots(){
        //TODO
    }

    public ArrayList<Restaurant> getRestaurants(){
        //TODO
    }

    public ArrayList<Review> getReviews(){
        //TODO
    }*/
}
