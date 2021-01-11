package com.example.location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity<mp> extends AppCompatActivity {
    private static final String SHARED_PREFERENCES = "shared_preferences";
    private static final String SAVED_LATITUDE = "saved_latitude";
    private static final String SAVED_LONGITUDE = "saved_longitude";
    private static final String SAVED_STRAY = "saved_stray";
    private static final String FIRST_OPEN = "first_open";
   // private static DecimalFormat df2 = new DecimalFormat("#.##");
    Button btnsave;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    TextView latitudeTV;
    TextView longitudeTV;
    TextView savedLat;
    TextView savedLon;
    int musicon;
    float savedlatvar;
    float savedlonvar;
    float latitude;
    int password;
    int appopendcount;
    float longitude;
    boolean firstAppOpen;
    boolean outsideLimit;
    MediaPlayer mp;
    EditText num2;
    boolean ON;
    TextView outside;
    int passcode;
    private CountAdder increaser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView num = (TextView) findViewById(R.id.textView6);
        increaser = new CountAdder();
        ON = false;
        Button one = (Button) findViewById(R.id.button);
        one.setVisibility(View.INVISIBLE);
        EditText num2 = (EditText)findViewById(R.id.editTextNumber2);
        num2.setVisibility(View.INVISIBLE);
        num.setText(String.valueOf(increaser.getCount()));
        latitudeTV = (TextView) findViewById(R.id.latitude);
        longitudeTV = (TextView) findViewById(R.id.longitude);
        savedLat = (TextView) findViewById(R.id.textView9);
        savedLon = (TextView) findViewById(R.id.textView10);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        loadSaved();
        activityOpener();
        add();
        addsmall();
        subtract();
        subtractsmall();
        savebutton();
        onButton();
        distanceButton();
        refreshLocation();
        timer();
        configureNextButton();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

           // getCurrentLocation();
        } else{
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION},44);
        }



    }
    public void onButton() {

        Button onButton = (Button) findViewById(R.id.button3);
        onButton.setText("OFF");
        onButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ON) {
                    ON = false;
                    onButton.setText("OFF");
                }
                else
                {
                    ON = true;
                    onButton.setText("ON");
                    timer();
                }
            }
        });
    }
    public void activityOpener()
    {
        if(firstAppOpen ) {
            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(intent);
            firstAppOpen = false;
            //appopendcount++;
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();


            editor.putBoolean(FIRST_OPEN, false);

            editor.apply();
        }
    }
    public void add() {

        ImageButton btnAdd = (ImageButton) findViewById(R.id.imageButton);
        TextView num = (TextView) findViewById(R.id.textView6);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TextView num = (TextView) findViewById(R.id.textView6);
                increaser.increaseCount();
                num.setText(String.valueOf(String.format("%.2f",increaser.getCount())));
            }
        });
    }
    public void addsmall() {

        ImageButton btnAddsmall = (ImageButton) findViewById(R.id.smallplus);
        TextView num = (TextView) findViewById(R.id.textView6);
        btnAddsmall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TextView num = (TextView) findViewById(R.id.textView6);
                increaser.increaseCountSmall();
                num.setText(String.valueOf(String.format("%.2f",increaser.getCount())));
            }
        });
    }
    public void subtract() {

        ImageButton btnSub = (ImageButton) findViewById(R.id.imageButton2);
        TextView num = (TextView) findViewById(R.id.textView6);
        btnSub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                increaser.decreaseCount();
                num.setText(String.valueOf(String.format("%.2f",increaser.getCount())));
            }
        });
    }
    public void subtractsmall() {

        ImageButton subtractSmall = (ImageButton) findViewById(R.id.smallminus);
        TextView num = (TextView) findViewById(R.id.textView6);
        subtractSmall.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                increaser.decreaseCountSmall();
                num.setText(String.valueOf(String.format("%.2f",increaser.getCount())));
            }
        });
    }
    public void distanceButton() {

        Button distbtn = (Button) findViewById(R.id.distancebutton);
        distbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView distanceTV = (TextView) findViewById(R.id.distancetv);
                TextView distanceTV2 = (TextView) findViewById(R.id.distancetv2);
                float distance = (float) (meterDistanceBetweenPoints(latitude, longitude, savedlatvar, savedlonvar)/1609);
                float distancemeters = (float) (meterDistanceBetweenPoints(latitude, longitude, savedlatvar, savedlonvar));
                distanceTV.setText(String.valueOf("Miles from save: " + String.format("%.2f",distance) ));
                distanceTV2.setText(String.valueOf("Meters: " + String.format("%.2f",distancemeters)));
            }
        });
    }
//public void getdist()
//{
//    TextView distanceTV = (TextView) findViewById(R.id.distancetv);
//    TextView distanceTV2 = (TextView) findViewById(R.id.distancetv2);
//    float distance = (float) (meterDistanceBetweenPoints(latitude, longitude, savedlatvar, savedlonvar)/1609);
//    float distancemeters = (float) (meterDistanceBetweenPoints(latitude, longitude, savedlatvar, savedlonvar));
//    distanceTV.setText(String.valueOf("Miles from save: " + String.format("%.2f",distance )));
//    distanceTV2.setText(String.valueOf("Meters: " + String.format("%.2f",distancemeters)));
//}
    private void loadSaved() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        float miles = sharedPreferences.getFloat(SAVED_STRAY, 0);
        float lat = sharedPreferences.getFloat(SAVED_LATITUDE, 0);
        float lon = sharedPreferences.getFloat(SAVED_LONGITUDE, 0);
        boolean firstOpen = sharedPreferences.getBoolean(FIRST_OPEN, true);
        int passcode1 = sharedPreferences.getInt("saved_passcode", 0);
        firstAppOpen = firstOpen;
        passcode = passcode1;
        savedlatvar = lat;
        savedlonvar = lon;
        TextView num = (TextView) findViewById(R.id.textView6);
        increaser.setValue(miles);
        num.setText(String.valueOf(String.format("%.2f",increaser.getCount())));

    }
    public void savebutton() {

       btnsave = (Button) findViewById(R.id.button2);
        btnsave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                savedlatvar = latitude;
                savedlonvar = longitude;
                saveLatLon();
                Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                savedLat.setText(String.valueOf("Saved Latitude: " + savedlatvar));
               savedLon.setText(String.valueOf("Saved Longitude: " + savedlonvar));
            }
        });
    }
    public void refreshLocation() {

        Button refreshbutton = (Button) findViewById(R.id.refresh);
        refreshbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getCurrentLocation();
            }
        });
    }
    private void saveMiles(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putFloat(SAVED_STRAY, (float) increaser.getCount());

        editor.apply();
    }
    private void saveLatLon(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        //editor.putFloat(SAVED_STRAY, (float) increaser.getCount());
        editor.putFloat(SAVED_LATITUDE, (float) savedlatvar);
        editor.putFloat(SAVED_LONGITUDE, (float) savedlonvar);
        //savedLat.setText(String.valueOf("Saved Latitude: " + savedlatvar));
        //savedLon.setText(String.valueOf("Saved Longitude: " + savedlonvar));
        editor.apply();
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager
                .PERMISSION_GRANTED && ActivityCompat.
                checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                            latitudeTV.setText("Current Latitude:" + (double) location.getLatitude());
                            latitude = (float) location.getLatitude();
                            longitudeTV.setText("Current Longitude:" + (double) location.getLongitude());
                            longitude = (float) location.getLongitude();
                            outside = (TextView)findViewById(R.id.textView12);
                            if( (float) (meterDistanceBetweenPoints(latitude, longitude, savedlatvar, savedlonvar)/1609)>increaser.getCount())
                            {
                                outsideLimit = true;
                                outside.setText("Outside Set Limit: Yes");
                                noise();
                            }
                            else
                            {
                                outsideLimit = false;
                                outside.setText("Outside Set Limit: No");
                            }

                                MarkerOptions options = new MarkerOptions().position(latLng).title("I am here")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.clipart2978265__1_));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                                googleMap.clear();
                                googleMap.addMarker(options);







                        }
                    });
                }
            }
        });

    }
    private double meterDistanceBetweenPoints(float lat_a, float lng_a, float lat_b, float lng_b) {
        float pk = (float) (180.f/Math.PI);
        float a1 = lat_a / pk;
        float a2 = lng_a / pk;
        float b1 = lat_b / pk;
        float b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);
        return 6366000 * tt;
    }
    @Override
    protected void onPause() {
        super.onPause();
        saveMiles();
        saveLatLon();
    }
    public void timer() {
        final Handler handler = new Handler();
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        if(ON) {
                            Toast.makeText(MainActivity.this, "Fetched Location", Toast.LENGTH_SHORT).show();
                            getCurrentLocation();
                            timer();
                        }
                    }
                });
            }
        }, 120000);
    }
    @Override
    protected void onResume() {
        super.onResume();
      //  getCurrentLocation();

        savedLat.setText(String.valueOf("Saved Latitude: " + savedlatvar));
        savedLon.setText(String.valueOf("Saved Longitude: " + savedlonvar));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 44){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation();
            }
        }
    }
    private void configureNextButton()
    {
        ImageButton button = (ImageButton) findViewById(R.id.imageButton5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);

            }
        });
    }

    public void noise()
    {
        Button one = (Button) findViewById(R.id.button);
        num2 = (EditText) findViewById(R.id.editTextNumber2);
        num2.setText("0000");
        num2.setVisibility(View.VISIBLE);
        one.setVisibility(View.VISIBLE);
            MediaPlayer mp = MediaPlayer.create(this, R.raw.soundforproject);
            mp.start();
        one.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String value= num2.getText().toString();
                password =Integer.parseInt(value);
                loadSaved();
                if(password == passcode)
                {
                    mp.stop();
                    one.setVisibility(View.INVISIBLE);
                    num2.setVisibility(View.INVISIBLE);

                }
                else
                {
                    num2.setBackgroundColor(Color.RED);
                }
            }
        });
    }

}