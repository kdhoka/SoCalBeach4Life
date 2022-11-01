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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    private Circle c;
    private int radius = 1000;
    private Marker current_marker;

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

        ArrayList<String> restaurantNames = new ArrayList<String>();
        ArrayList<String> Lats = new ArrayList<String>();
        ArrayList<String> Lngs = new ArrayList<String>();

        ValueEventListener beachCredentialListener = new ValueEventListener() {
            private static final String TAG = "Beach read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                int size = 0;
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    String restName = dsp.child("name").getValue().toString();
                    String lat = dsp.child("xpos").getValue().toString();
                    String lng = dsp.child("ypos").getValue().toString();
                    restaurantNames.add(restName);
                    Lats.add(lat);
                    Lngs.add(lng);
                    size++;
                }
                TextView tv = findViewById(R.id.text);

                ListView lv = findViewById(R.id.list);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, restaurantNames);
                lv.setAdapter(arrayAdapter);

                ListView lv2 = findViewById(R.id.list2);
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Lats);
                lv2.setAdapter(arrayAdapter2);

                ListView lv3 = findViewById(R.id.list3);
                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, Lngs);
                lv3.setAdapter(arrayAdapter3);

                tv.setText(String.valueOf(size));

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
            sayHello(currentUser);
        }
    }

    private void sayHello(FirebaseUser currentUser) {
        TextView helloText = findViewById(R.id.hello);
        StringBuilder builder = new StringBuilder("Hello, " + currentUser.getEmail());
        helloText.setText(builder);
    }

    public void onClickLogReg(View view){
        Intent intent = new Intent(this, LogInRegisterActivity.class);
        startActivity(intent);
    }

    public void onClickBeach(View view){
        Intent intent = new Intent(this, BeachInfoActivity.class);
        startActivity(intent);
    }

    public void onClickRestaurant(View view){
        Intent intent = new Intent(this, RestaurantPage.class);
        startActivity(intent);
    }

    public void onLoadFinished(){
        TextView tv = findViewById(R.id.text);
        ListView lv = findViewById(R.id.list);
        ListView lv2 = findViewById(R.id.list2);
        ListView lv3 = findViewById(R.id.list3);
        for(int i = 0; i < Integer.valueOf((String)tv.getText()); ++i){
            String name = (String) lv.getItemAtPosition(i);
            Double la = Double.valueOf((String)lv2.getItemAtPosition(i));
            Double ln = Double.valueOf((String)lv3.getItemAtPosition(i));
            mMap.addMarker(new MarkerOptions().position(new LatLng(la, ln)).title(name));
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