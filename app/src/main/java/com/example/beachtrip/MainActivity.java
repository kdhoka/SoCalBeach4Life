package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.beachtrip.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    private Circle c;
    private int radius = 1000;
    private Marker current_marker = null;
    private ArrayList<Beach> beachList;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //map basic settings
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //dropdown menu setting
        Spinner rangeSpinner = findViewById(R.id.rangeChoice);
        rangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {

                radius = Integer.valueOf(rangeSpinner.getSelectedItem().toString());
                updateMarker();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        rangeSpinner.setBackgroundColor(Color.CYAN);
        ArrayAdapter<CharSequence>adapter = ArrayAdapter.createFromResource(this, R.array.ranges, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        rangeSpinner.setAdapter(adapter);

        //read and display beach markers
        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference beachRef= root.getReference("beaches"); //pointer to the Beach tree

        ValueEventListener beachCredentialListener = new ValueEventListener() {
            private static final String TAG = "Beach read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                beachList = new ArrayList<>();
                int size = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String id = dsp.getKey().toString();
                    String Name = dsp.child("name").getValue().toString();
                    Double lat = Double.valueOf(dsp.child("xpos").getValue().toString());
                    Double lng = Double.valueOf(dsp.child("ypos").getValue().toString());
                    beachList.add(new Beach(id, Name, new LatLng(lat, lng),new String[1][1], new ParkingLot[1], new Review[1]));
                }

                onLoadFinished();
                // ..
            }

            @Override //trigger when the client(us) doesn't have permission to read from the DB
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };

        beachRef.addValueEventListener(beachCredentialListener);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
//            sayHello(currentUser);
        }
    }

//    private void sayHello(FirebaseUser currentUser) {
//        TextView helloText = findViewById(R.id.hello);
//        helloText.setText("Hello, " + currentUser.getDisplayName());
//    }

    public void onClickLogReg(View view){
        Intent intent = new Intent(this, LogInRegisterActivity.class);
        startActivity(intent);
    }

    public void onClickBeach(View view){

        if(current_marker != null){
            Intent intent = new Intent(this, BeachInfoActivity.class);
            String id = (String) current_marker.getTag();
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please click on a marker before proceeding.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRestaurant(View view){
        Intent intent = new Intent(this, RestaurantPage.class);
        startActivity(intent);
    }

    public void onLoadFinished(){
        Marker temp;
        for(Beach b: beachList){
            temp = mMap.addMarker(new MarkerOptions().position(b.getLocation()).title(b.getName()));
            temp.setTag(b.getID());
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*
        //dump markers
        LatLng la01 = new LatLng(34.05, -118.24);
        LatLng la02 = new LatLng(34.15, -118.14);
        mMap.addMarker(new MarkerOptions().position(la01).title("Marker in LA 01"));
        mMap.addMarker(new MarkerOptions().position(la02).title("Marker in LA 02"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(la01));
        */
        LatLng la = new LatLng(34.05, -118.24);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(la, 9));

        mMap.setOnMarkerClickListener(this);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        current_marker = marker;
        updateMarker();

        return false;
    }

    public void updateMarker(){
        if(current_marker == null){
            return;
        }
        LatLng center = current_marker.getPosition();
        if(c != null){
            c.remove();
        }
        c = mMap.addCircle(new CircleOptions()
                .center(center)
                .radius(radius)
                .strokeColor(Color.BLACK)
                .fillColor(getResources().getColor(R.color.semi_blue)));
    }

}