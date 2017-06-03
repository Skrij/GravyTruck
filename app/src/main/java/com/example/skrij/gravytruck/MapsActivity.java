package com.example.skrij.gravytruck;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.skrij.gravytruck.fragments.InformationFragment;
import com.example.skrij.gravytruck.fragments.MenuFragment;
import com.example.skrij.gravytruck.fragments.ParentFragment;
import com.example.skrij.gravytruck.fragments.RecommendationFragment;
import com.example.skrij.gravytruck.helpers.SystemPrefsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        InformationFragment.OnFragmentInteractionListener,
        MenuFragment.OnFragmentInteractionListener,
        RecommendationFragment.OnFragmentInteractionListener,
        ParentFragment.OnFragmentInteractionListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private String distance;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker mFoodtruck;
    LocationRequest mLocationRequest;
    ParentFragment _parentFragment;
    Polyline polylineFinal;
    Integer pathDraw = 0;

    //private Marker previousMarker;
    private String foodtruckImage;
    //private String previousTruckImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Give a custom typography to the title
        TextView button_text = (TextView) findViewById(R.id.go_button);
        Typeface oaf = Typeface.createFromAsset(getAssets(), "fonts/OAF.otf");
        button_text.setTypeface(oaf);

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        _parentFragment = new ParentFragment();
        ft.replace(R.id.parent_fragment_container, _parentFragment);
        ft.commit();

    }

    // Prevent from returning to the sign uo / in activity
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Hide the informations layout
        final ConstraintLayout mLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_maps);
        final Button mButton = (Button) findViewById(R.id.go_button);
        ImageButton profile_button = (ImageButton) findViewById(R.id.profile_button);
        mLayout.setVisibility(ConstraintLayout.GONE);
        mButton.setVisibility(ConstraintLayout.GONE);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        // Get all the trucks' position and display a market
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = getResources().getString(R.string.api_address) + "/trucks?key=" + getResources().getString(R.string.api_key);

        JsonObjectRequest request_json = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray trucks = response.getJSONArray("trucks");
                            for (int i = 0; i < trucks.length(); i++) {
                                JSONObject obj = trucks.getJSONObject(i);
                                // Get the position
                                LatLng latlngfood = new LatLng(obj.getDouble("lat"), obj.getDouble("lng"));
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latlngfood);
                                // Get truck's hash
                                markerOptions.title(obj.getString("hash"));
                                // Set a specific image
                                if (Objects.equals(obj.getString("food"), "burger")) {
                                    foodtruckImage = "truck_burger";
                                } else if (Objects.equals(obj.getString("food"), "glace")) {
                                    foodtruckImage = "truck_glace";
                                } else if (Objects.equals(obj.getString("food"), "pizza")) {
                                    foodtruckImage = "truck_pizza";
                                } else if (Objects.equals(obj.getString("food"), "tacos")) {
                                    foodtruckImage = "truck_tacos";
                                } else {
                                    foodtruckImage = "truck_hot_dog";
                                }
                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(foodtruckImage, 150, 100)));
                                mFoodtruck = mMap.addMarker(markerOptions);
                            }
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "No trucks found :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be executed
        queue.add(request_json);

        // Handle click on marker
        mMap.setOnMarkerClickListener(this);

        // Hide the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLayout.setVisibility(ConstraintLayout.GONE);
                mButton.setVisibility(ConstraintLayout.GONE);
            }
        });

        // Go to profile activity on click
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile_intent = new Intent(MapsActivity.this, ProfileActivity.class);
                startActivity(profile_intent);
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Draw circle around us
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(location.getLatitude(), location.getLongitude()))
                .radius(500)
                .strokeColor(Color.RED));
        circle.setStrokeWidth(5);

        // Move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        // Stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Cannot keep the marker image with it
//        if(previousMarker!=null) {
//            previousMarker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(foodtruckImage, 150, 100)));
//        }
//
//        previousMarker = marker;
//        marker.setIcon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons(foodtruckImage, 175, 125)));

        // Remove path if there is already one
        if (pathDraw >= 1) {
            polylineFinal.remove();
        }

        // Get all the truck's informations and pass them to the fragments
        RequestQueue queue = Volley.newRequestQueue(this);
        final String truck_hash = marker.getTitle();
        getDistance(marker);

        // Informations fragment

        String url = getResources().getString(R.string.api_address) + "/truck/" + truck_hash + "?key=" + getResources().getString(R.string.api_key);

        JsonObjectRequest request_json = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            _parentFragment.jsonTruckInformations = response;
                            _parentFragment.UpdateSubfragments();
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "No data :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be execute
        queue.add(request_json);

        // Menu fragment

        url = getResources().getString(R.string.api_address) + "/truck/" + truck_hash + "/foods?key=" + getResources().getString(R.string.api_key);

        request_json = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            _parentFragment.jsonTruckMenu = response;
                            _parentFragment.UpdateSubfragments();
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be executed
        queue.add(request_json);

        // Recommendation fragment

        url = getResources().getString(R.string.api_address) + "/truck/" + truck_hash + "/comments?key=" + getResources().getString(R.string.api_key);

        request_json = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            _parentFragment.jsonTruckComment = response;
                            _parentFragment.truckHash = truck_hash;
                            _parentFragment.UpdateSubfragments();
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be executed
        queue.add(request_json);

        // Get data
        final HashMap<String, String> params = new HashMap<>();
        params.put("user_hash", SystemPrefsHelper.getInstance(this).getSystemPrefString("hash"));
        params.put("truck_hash", truck_hash);

        // Check if the truck is liked or not

        url = getResources().getString(R.string.api_address) + "/like-check?key=" + getResources().getString(R.string.api_key);

        request_json = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            _parentFragment.jsonLikeState = response;
                            _parentFragment.UpdateSubfragments();
                        } catch (Exception e) {
                            Toast.makeText(MapsActivity.this, "Something went wrong :(", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        // Add the request object to the queue to be executed
        queue.add(request_json);

        ConstraintLayout mLayout = (ConstraintLayout) findViewById(R.id.constraint_layout_maps);
        Button mButton = (Button) findViewById(R.id.go_button);
        ImageButton profile_button = (ImageButton) findViewById(R.id.profile_button);
        mLayout.setVisibility(ConstraintLayout.VISIBLE);
        mButton.setVisibility(ConstraintLayout.VISIBLE);

        // Draw the shortest path on click
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pathDraw >= 1) {
                    polylineFinal.remove();
                    pathDraw = pathDraw - 1;
                    DrawPolylines(marker);
                    pathDraw = pathDraw + 1;
                } else {
                    DrawPolylines(marker);
                    pathDraw = pathDraw + 1;
                }
            }
        });

        return true;
    }

    // Get the path to go to a foodtruck by walking and display it
    public void DrawPolylines(final Marker marker) {
        Location location = mLastLocation;
        LatLng hereWeAre = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng foodTruckLocation = marker.getPosition();
        GoogleDirection.withServerKey(getString(R.string.API_DIRECTION_KEY))
                .from(hereWeAre)
                .to(foodTruckLocation)
                .transportMode(TransportMode.WALKING)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        PolylineOptions polylineOptions = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 3, Color.RED);
                        polylineFinal = mMap.addPolyline(polylineOptions);
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });
    }

    // Get the distance between us and the foodtruck
    public void getDistance(final Marker marker) {
        Location location = mLastLocation;
        LatLng hereWeAre = new LatLng(location.getLatitude(), location.getLongitude());
        LatLng foodTruckLocation = marker.getPosition();
        GoogleDirection.withServerKey(getString(R.string.API_DIRECTION_KEY))
                .from(hereWeAre)
                .to(foodTruckLocation)
                .transportMode(TransportMode.WALKING)
                .unit(Unit.METRIC)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        Route route = direction.getRouteList().get(0);
                        Leg leg = route.getLegList().get(0);
                        Info distanceInfo = leg.getDistance();
                        distance = distanceInfo.getText();
                        _parentFragment.truckDistance = distance;
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something here
                    }
                });
    }

    // Resize the marker icon
    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void messageFromChildFragment(Uri uri) {

    }

    @Override
    public void messageFromParentFragment(Uri uri) {

    }
}
