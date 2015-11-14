package com.cecs453.assignment_03;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private  static final String TAG = "com.cecs453.assignment_03";
    private PowerManager.WakeLock wakeLock;
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeVariable();
        gameView = new GameView(this);
        setContentView(gameView);
    }

    private void initializeVariable() {
        PowerManager powerManager = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        gameView.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.stopGame();
        wakeLock.release();

    }

    //GameView class with Sensor operations and the ilk
    private class GameView extends View implements SensorEventListener{

        private SensorManager sensorManager;
        private Sensor accelerometer;
        private Display display;

        private Bitmap background;
        private Bitmap ball;
        private Bitmap goal;

        private static  final int BALL_SIZE = 100;
        private static  final int GOAL_SIZE = 400;

        private float xOrigin;
        private float yOrigin;
        private float horizontalBound;
        private float verticalBound;

        private float sensorX;
        private float sensorY;
        private float sensorZ;
        private long timeStamp;

        private Particle b;

        public GameView(Context context){
            super(context);
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();

            sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            Bitmap ball_cache = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
            ball = Bitmap.createScaledBitmap(ball_cache, BALL_SIZE, BALL_SIZE, true);
            Bitmap goal_cache = BitmapFactory.decodeResource(getResources(), R.drawable.basketball_hoop);
            goal = Bitmap.createScaledBitmap(goal_cache, GOAL_SIZE, GOAL_SIZE, true);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inDither = true;
            opts.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bg_cache = BitmapFactory.decodeResource(getResources(), R.drawable.background);
            background = Bitmap.createScaledBitmap(bg_cache,1100,1550,false);


            b = new Particle();


        }

        public void startGame(){
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        public void stopGame(){
            sensorManager.unregisterListener(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(background, 0, 0, null);
            canvas.drawBitmap(goal, xOrigin - GOAL_SIZE/2, yOrigin - GOAL_SIZE/2, null);

            b.updatePosition(sensorX, sensorY, sensorZ, timeStamp);
            b.collision(horizontalBound, verticalBound);

            canvas.drawBitmap(ball, (xOrigin - BALL_SIZE/2) + b.positionX, (yOrigin - BALL_SIZE/2) - b.positionY, null);

            invalidate();
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            xOrigin = w * 0.5f;
            yOrigin = h * 0.5f;
            horizontalBound = (w - BALL_SIZE) * 0.5f;
            verticalBound = (h - BALL_SIZE) * 0.5f;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
                return;

            switch (display.getRotation()) {
                case Surface.ROTATION_0:
                    sensorX = event.values[0];
                    sensorY = event.values[1];
                    break;
                case Surface.ROTATION_90:
                    sensorX = -event.values[1];
                    sensorY = event.values[0];
                    break;
                case Surface.ROTATION_180:
                    sensorX = -event.values[0];
                    sensorY = -event.values[1];
                    break;
                case Surface.ROTATION_270:
                    sensorX = event.values[1];
                    sensorY = -event.values[0];
                    break;
            }
            sensorZ = event.values[2];
            timeStamp = event.timestamp;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }

    private class Particle {
        private static final float BOUNCE = 0.8f;
        private static final float MAX_VELOCITY = 0.000001f;
        private static final float MAX_DELTA = 0.000001f;



        public float positionX;
        public float positionY;
        public float velocityX;
        public float velocityY;

        public void updatePosition(float sensorX, float sensorY, float sensorZ, long timeStamp){
            float deltaTime = (System.nanoTime() - timeStamp) / 100000000000.0f;
            if(deltaTime > MAX_DELTA)deltaTime = MAX_DELTA;
            velocityX += -sensorX * deltaTime;
            velocityY += -sensorY * deltaTime;

            if(velocityX > MAX_VELOCITY) velocityX = MAX_VELOCITY;
            if(velocityX < -MAX_VELOCITY) velocityX = -MAX_VELOCITY;
            if(velocityY > MAX_VELOCITY) velocityY = MAX_VELOCITY;
            if(velocityY < -MAX_VELOCITY) velocityY = -MAX_VELOCITY;

            positionX += velocityX * deltaTime;
            positionY += velocityY * deltaTime;

        }

        public void collision(float horizontalBound, float verticalBound){
            if(positionX > horizontalBound){
                positionX = horizontalBound;
                velocityX = -velocityX * BOUNCE;
            } else if(positionX < - horizontalBound){
                positionX = -horizontalBound;
                velocityX = -velocityX * BOUNCE;
            }

            if(positionY > verticalBound){
                positionY = verticalBound;
                velocityY = -velocityY * BOUNCE;
            } else if(positionY < - verticalBound){
                positionY = -verticalBound ;
                velocityY = -velocityY * BOUNCE;
            }

        }

    }
}
