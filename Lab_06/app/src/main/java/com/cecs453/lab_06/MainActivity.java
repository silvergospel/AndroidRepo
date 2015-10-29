package com.cecs453.lab_06;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private TextView temperatureTextView;
    private TextView pressureTextView;
    private TextView lightTextView;

    private float currentTemperature = Float.NaN;
    private float currentPressure = Float.NaN;
    private float currentLight = Float.NaN;

    private SensorEventListener tempSensorEventListener;
    private SensorEventListener pressureSensorEventListener;
    private SensorEventListener lightSensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariables();

    }
    @Override
    protected void onResume(){
        super.onResume();
        Sensor temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if(temperatureSensor != null)
            sensorManager.registerListener(tempSensorEventListener, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        else temperatureTextView.setText("NO TEMP SENSOR");

        Sensor  pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressureSensor != null)
            sensorManager.registerListener(pressureSensorEventListener, pressureSensor,SensorManager.SENSOR_DELAY_NORMAL);
        else pressureTextView.setText("NO PRESSURE SENSOR");

        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor != null) sensorManager.registerListener(lightSensorEventListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);
        else lightTextView.setText("NO LIGHT SENSOR");
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(tempSensorEventListener);
        sensorManager.unregisterListener(pressureSensorEventListener);
        sensorManager.unregisterListener(lightSensorEventListener);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeVariables(){
        //initialize TextViews
        temperatureTextView = (TextView) findViewById(R.id.temperature);
        pressureTextView = (TextView) findViewById(R.id.pressure);
        lightTextView = (TextView) findViewById(R.id.light);
        //create timer for screen updates
        Timer updateTimer = new Timer("weatherUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateGUI();
            }
        }, 0, 1000);
        //instantiate listeners

        tempSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                currentTemperature = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        pressureSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                currentPressure = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        lightSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                currentLight = event.values[0];
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private  void updateGUI(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //update temp
                if(!Float.isNaN(currentTemperature)){
                    temperatureTextView.setText(currentTemperature + "C");
                    temperatureTextView.invalidate();
                }
                //update pressure
                if(!Float.isNaN(currentPressure)){
                    pressureTextView.setText(currentPressure + "(mBars)");
                    pressureTextView.invalidate();
                }
                if (!Float.isNaN(currentLight)){
                    String lightStr = "Sunny";
                    if(currentLight <= SensorManager.LIGHT_CLOUDY) lightStr = "Night";
                    else if(currentLight <= SensorManager.LIGHT_OVERCAST) lightStr="Cloudy";
                    else if(currentLight <= SensorManager.LIGHT_SUNLIGHT) lightStr = "Overcast";

                    lightTextView.setText(lightStr);
                    lightTextView.invalidate();
                }
            }
        });
    }
}
