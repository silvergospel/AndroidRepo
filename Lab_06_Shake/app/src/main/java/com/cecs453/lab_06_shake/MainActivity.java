package com.cecs453.lab_06_shake;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private boolean colorToggle = false;
    private View view;
    private long lastUpdate;
    TextView textx, texty, textz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initializeVariables();
    }

    private void initializeVariables(){
        textx = (TextView) findViewById(R.id.xVal);
        texty = (TextView) findViewById(R.id.yVal);
        textz = (TextView) findViewById(R.id.zVal);

        view = (TextView) findViewById(R.id.colorText);
        if(view != null) view.setBackgroundColor(Color.BLUE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdate = System.currentTimeMillis();

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            displayAccelerometer(event);
            checkShake(event);
        }
    }

    private void displayAccelerometer(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        if(textx != null)textx.setText("X axis = \t\t" + x);
        if(texty != null)texty.setText("Y axis = \t\t" + y);
        if(textz != null)textz.setText("Z axis = \t\t" + z);
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override

    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    private void checkShake(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float accelerationSquareRoot = (x * x + y * y + z * z)/(SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if(accelerationSquareRoot >= 2){
            if(actualTime - lastUpdate < 200){
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(MainActivity.this, "Feeling a lil shaky!", Toast.LENGTH_SHORT).show();
            if(colorToggle){
                if(view != null)view.setBackgroundColor(Color.BLUE);
            } else {
                if(view != null)view.setBackgroundColor(Color.RED);
            }
            colorToggle = !colorToggle;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



}
