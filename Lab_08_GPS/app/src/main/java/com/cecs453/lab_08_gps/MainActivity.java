package com.cecs453.lab_08_gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    Button btnShowLocation;
    GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnShowLocation = (Button)findViewById(R.id.btn_show_location);
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);
                if(gps.canGetLocation()){
                    gps.getLocation();
                    double lat = gps.getLatitude();
                    double lon = gps.getLongitude();
                    Toast.makeText(getApplicationContext(),"Your location is:\nLat: " + lat + "\nLon: " +lon, Toast.LENGTH_SHORT).show();
                }else{
                    gps.showSettingsAlert();
                }
            }
        });
    }
}
