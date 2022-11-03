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
import android.os.AsyncTask;
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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.beachtrip.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.libraries.places.api.Places;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
    private Polyline current_route = null;
    private ArrayList<Beach> beachList;
    private ArrayList<Restaurant> restaurantList;
    private ArrayList<Marker> restMarkers = new ArrayList();
    private Marker curRestMarker;

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
                    String id = dsp.getKey();
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

    public void onClickRoute(View view){
        if(user_marker == null){
            Toast.makeText(MainActivity.this, "Please update your current location before proceeding.", Toast.LENGTH_SHORT).show();
        } else if (current_marker == null) {
            Toast.makeText(MainActivity.this, "Please click on a marker before proceeding.", Toast.LENGTH_SHORT).show();
        } else {
            String url = getRouteURL(user_marker.getPosition(), current_marker.getPosition(), "AIzaSyAbnF-bckeq1b-E-nzZTrUFEQqVhzncg_w");
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
        }
    }

    public String getRouteURL(LatLng origin, LatLng dest, String key){
        return "https://maps.googleapis.com/maps/api/directions/json?origin="+origin.latitude+","+origin.longitude+
                "&destination="+dest.latitude+","+dest.longitude+
                "&sensor=false" +
                "&mode=driving" +
                "&key="+key;
    }

    public void onClickBeach(View view){
        if(current_marker != null){
            Intent intent = new Intent(this, BeachInfoActivity.class);
            String id = (String) current_marker.getTag();
            intent.putExtra("id", id);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "Please click on a Beach before proceeding.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickRestaurant(View view){
        if(curRestMarker == null){
            Toast.makeText(MainActivity.this, "Please click on a Restaurant before proceeding.",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, RestaurantPage.class);
            intent.putExtra("restaurantName", (String) curRestMarker.getTitle());
            startActivity(intent);
        }
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
        /*
        LatLng la01 = new LatLng(33.983993723959344, -118.4572885881826);
        mMap.addMarker(new MarkerOptions().position(la01).title("Restaurant"));
         */

        LatLng la = new LatLng(34.05, -118.24);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(la, 9));

        mMap.setOnMarkerClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(restMarkers.contains(marker)){
            curRestMarker = marker;
            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText(marker.getTitle());
        }
        else{
            current_marker = marker;
            updateMarker();
        }

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

        displayRestaurants();

        /*
        LatLng la01 = new LatLng(33.983993723959344, -118.4572885881826);
        float[] results = new float[1];
        Location.distanceBetween(center.latitude, center.longitude,
                la01.latitude, la01.longitude, results);

        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText(String.valueOf(results[0] * 3.28084));*/
    }

    private void displayRestaurants(){
        if(current_marker == null){
            return;
        }
        LatLng cur = current_marker.getPosition();

        for(Marker m : restMarkers){
            if(curRestMarker == m){
                curRestMarker = null;
            }
            m.remove();
        }
        restMarkers = new ArrayList<Marker>();

        FirebaseDatabase root = FirebaseDatabase.getInstance();
        DatabaseReference restRef= root.getReference("Restaurants"); //pointer to the Restaurant tree
        restaurantList = new ArrayList<Restaurant>();
        ValueEventListener restaurantCredentialListener = new ValueEventListener() {
            private static final String TAG = "Restaurant read.";

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get value of each attribute of a User ob
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Double Lat = (Double) dsp.child("Lat").getValue();
                    Double Lng = (Double) dsp.child("Lng").getValue();
                    float[] results = new float[1];
                    Location.distanceBetween(cur.latitude, cur.longitude,
                            Lat, Lng, results);
                    if(results[0] * 3.28084 <= radius){
                        String restName = dsp.child("name").getValue().toString();
                        Restaurant r = new Restaurant(restName, null, null, new LatLng(Lat, Lng));
                        restaurantList.add(r);
                    }
                }
                drawRestaurants();
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

    private void drawRestaurants(){
        for(Restaurant r : restaurantList){
            Marker rMarker = mMap.addMarker(new MarkerOptions().position(r.getLocation()).title(r.getName()));
            restMarkers.add(rMarker);
        }
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

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.RED);
                lineOptions.geodesic(true);

            }
            if(current_route != null){
                current_route.remove();
            }
            current_route = mMap.addPolyline(lineOptions);
        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
