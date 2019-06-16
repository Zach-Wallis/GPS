package au.edu.federation.gps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private LocationListener locationListener;
    private LocationManager locationManager;

    private final long MIN_TIME = 1000; // 1 sec
    private final long MIN_DIST = 5;//5 meters

    private LatLng LatLng;
    private String code = "BLANK";

    final int delay = 1000;//milliseconds

    final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);





    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(2);
        mMap.setMinZoomPreference(17.0f);
        mMap.setMaxZoomPreference(19.0f);

        final RequestQueue queue = Volley.newRequestQueue(this);

        final MediaPlayer ping = MediaPlayer.create(this, R.raw.sonar_ping_sound_effect);

        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {

                try{
                    LatLng = new LatLng(location.getLatitude(),location.getLongitude());

                    String url = "https://plus.codes/api?address=" + LatLng.latitude  + ", " + LatLng.longitude;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    code = response.substring(39,50);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            code = "That didn't work!";
                        }
                    });
                    queue.add(stringRequest);

                    mMap.addMarker(new MarkerOptions().position(LatLng).title(code));
                    float zoomLevel = 19.0f;
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng, zoomLevel));

                    if(code.equals("4RJ59VFR+8P") || code.equals("4RJ59VFR+8Q") || code.equals("4RJ59VFR+7Q") || code.equals("4RJ59VFR+7P") || code.equals("4RJ5CVFG+6R")){
                        ping.start();
                    }
                }catch(SecurityException e){
                    e.printStackTrace();
                }




                /*handler.postDelayed(new Runnable() {                     ---------------------------------Method Call Repeater
                    @Override
                    public void run() {




                        handler.postDelayed(this, delay);
                    }
                }, delay);*/


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
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch(SecurityException e){
            e.printStackTrace();
        }

    }
}
