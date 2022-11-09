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

import org.json.JSONArray;
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
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, LocationListener {
    private static final String TAG = "log out from main page";
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
    private String ETA;
    private String[] route_details = null;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

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
        //NOTE: debug check DB connection
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    System.out.println("DB connected");
                } else {
                    System.out.println("DB not connected");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

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
                    String hours = dsp.child("hours").getValue().toString();
                    ArrayList<ParkingLot> parking = new ArrayList<>();
                    for(DataSnapshot lots : dsp.child("lots").getChildren()){
                        String lot_key = lots.getKey();
                        String lot_name = lots.child("name").getValue().toString();
                        Double lot_lat = Double.valueOf(lots.child("xpos").getValue().toString());
                        Double lot_lng = Double.valueOf(lots.child("ypos").getValue().toString());
                        parking.add(new ParkingLot(lot_key,lot_name,new LatLng(lot_lat, lot_lng)));
                        Log.i("database check", "Inserted lot!");
                    }
                    beachList.add(new Beach(id, Name, new LatLng(lat, lng),hours, parking, new ArrayList<>()));
                }

                Log.i("Check in", "Stepping into onLoadFinished()");
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


        //static user profile and log off button. Only activate activity is user is logged in.
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        TextView logMsg = findViewById(R.id.logMsg);
        if(currentUser != null) {
            currentUser.reload();
            String[] name_val = {""};
            DatabaseReference name = root.getReference().child("Users").child(currentUser.getUid()).child("name");
            name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    name_val[0] = snapshot.getValue(String.class);
                    logMsg.setText("Hi, " + name_val[0] + "!");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("read user name", "Failed to read current user name from DB", error.toException());
                }
            });
        } else {
            logMsg.setText("Please sign in to start");
        }
    }

    public void onClickLogReg(View view){
        Intent intent = new Intent(this, LogInRegisterActivity.class);
        startActivity(intent);
    }

    public void onClickSave(View view){
        if(current_route == null){
            Toast.makeText(MainActivity.this, "Please make a route before saving.",
                    Toast.LENGTH_SHORT).show();
        } else if(currentUser == null) {
            Toast.makeText(MainActivity.this, "Please login before saving.",
                    Toast.LENGTH_SHORT).show();
        } else if(route_details[3] != null){
            Toast.makeText(MainActivity.this, "This route has already been saved.",
                    Toast.LENGTH_SHORT).show();
        } else {
            Trip t = new Trip(currentUser.getUid(), route_details[0], route_details[1], ETA, route_details[2]);
            FirebaseDatabase root = FirebaseDatabase.getInstance();
            DatabaseReference tripsRef= root.getReference("trips");
            tripsRef.push().setValue(t);
            Toast.makeText(MainActivity.this, "Successfully saved trip.",
                    Toast.LENGTH_SHORT).show();
            route_details[3] = "";
        }
    }

    public void onClickLogOut(View view) {
        if (currentUser != null){
            mAuth.signOut();
            //check if sign out successfully, display toast
            if(mAuth.getCurrentUser() == null){
                currentUser = null;
                Log.d(TAG, "Logout:success");
                Toast.makeText(MainActivity.this, "Logged out successfully",
                        Toast.LENGTH_SHORT).show();
                TextView logOutMsg = findViewById(R.id.logMsg);
                logOutMsg.setText("Please sign in to start");
            } else {
                TextView logOutMsg = findViewById(R.id.logMsg);
                logOutMsg.setText("Failed to log out");
                Toast.makeText(MainActivity.this, "Logged out fails.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

//    public void onClickRoute(View view){
//        if(user_marker == null){
//            Toast.makeText(MainActivity.this, "Please update your current location before proceeding.", Toast.LENGTH_SHORT).show();
//        } else if (current_marker == null) {
//            Toast.makeText(MainActivity.this, "Please click on a marker before proceeding.", Toast.LENGTH_SHORT).show();
//        } else {
//            String url = getRouteURL(user_marker.getPosition(), current_marker.getPosition(), "AIzaSyAbnF-bckeq1b-E-nzZTrUFEQqVhzncg_w");
//            DownloadTask downloadTask = new DownloadTask();
//            downloadTask.execute(url);
//        }
//    }

    public String getRouteURL(LatLng origin, LatLng dest, String key, String mode){
        return "https://maps.googleapis.com/maps/api/directions/json?origin="+origin.latitude+","+origin.longitude+
                "&destination="+dest.latitude+","+dest.longitude+
                "&sensor=false" +
                "&mode="+mode +
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
            for(ParkingLot p: b.getParkingLots()){
                temp = mMap.addMarker(new MarkerOptions().position(p.getLocation()).title(p.getName()));
                temp.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                temp.setTag(p.getId());
            }
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
            String url = getRouteURL(current_marker.getPosition(), marker.getPosition(), "AIzaSyAbnF-bckeq1b-E-nzZTrUFEQqVhzncg_w", "walking");
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
            route_details = new String[4];
            route_details[0] = current_marker.getTitle();
            route_details[1] = marker.getTitle();
            route_details[2] = "Walking";
            route_details[3] = null;
        } else if(marker.getTag().toString().contains("lot")) {
            String url = getRouteURL(user_marker.getPosition(), marker.getPosition(), "AIzaSyAbnF-bckeq1b-E-nzZTrUFEQqVhzncg_w","driving");
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);
            route_details = new String[4];
            route_details[0] = user_marker.getTitle();
            route_details[1] = marker.getTitle();
            route_details[2] = "Driving";
            route_details[3] = null;
        } else {
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
                .strokeColor(Color.CYAN)
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
            rMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            rMarker.setTag(r.getName());
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
            user_marker.setTag("user");

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

    public void onClickProfile(View view) {
        if (currentUser != null){
            String uid = currentUser.getUid();
            Intent intent = new Intent(this, userProfileActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
    }

    public void onClickTrips(View view) {
        if (currentUser != null){
            String uid = currentUser.getUid();
            Intent intent = new Intent(this, TripListActvity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        }
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
                JSONArray jLegs = ( (JSONObject)jObject.getJSONArray("routes").get(0)).getJSONArray("legs");
                JSONObject DurAray = jLegs.getJSONObject(0);
                JSONObject durationObj = new JSONObject(DurAray.get("duration").toString());
                ETA = durationObj.getString("text");
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
            Toast.makeText(MainActivity.this, "ETA: " + ETA,
                    Toast.LENGTH_SHORT).show();
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
