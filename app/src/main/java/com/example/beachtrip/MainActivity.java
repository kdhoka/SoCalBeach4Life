package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Context context;
    private GoogleMap mMap;
    private ActivityMainBinding binding;
    private Circle c;
    private int radius = 1000;
    private Marker current_marker = null;
    private Marker user_marker = null;
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

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);


//        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            currentUser.reload();
//            sayHello(currentUser);
//        }

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
    }

//    private void sayHello(FirebaseUser currentUser) {
//        TextView helloText = findViewById(R.id.hello);
//        StringBuilder builder = new StringBuilder("Hello, " + currentUser.getEmail());
//        helloText.setText(builder);
//    }

    public void onClickLogReg(View view){
        Intent intent = new Intent(this, LogInRegisterActivity.class);
        startActivity(intent);
    }

    public  void onClickRoute(View view){

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
    public Marker makeUserMarker(LatLng loc){
        return mMap.addMarker(new MarkerOptions().position(loc).title("Your current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
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
        LatLng la01 = new LatLng(33.983993723959344, -118.4572885881826);
        mMap.addMarker(new MarkerOptions().position(la01).title("Restaurant"));

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
                .radius(radius / 3.28084)
                .strokeColor(Color.BLACK)
                .fillColor(getResources().getColor(R.color.semi_blue)));

        LatLng la01 = new LatLng(33.983993723959344, -118.4572885881826);
        float[] results = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                la01.latitude, la01.longitude, results);

        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(String.valueOf(results[0] * 3.28084));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(mMap != null){
            if(user_marker != null){
                user_marker.remove();
            }
            user_marker = makeUserMarker(new LatLng(location.getLatitude(),location.getLongitude()));
            user_marker.setVisible(true);
            System.out.println(user_marker.toString());

        } else {
            System.out.println("Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
}
