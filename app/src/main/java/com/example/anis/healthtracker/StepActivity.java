package com.example.anis.healthtracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class StepActivity extends AppCompatActivity implements SensorEventListener {

    final private int DAILY_STEPS_LIMIT = 8000;
    final private String NOTE_DAILY_STEPS = " more steps to go for today!";

    private SensorManager sensorManager;
    private TextView pedometerTextView;
    private TextView noteStepsTextView;
    private boolean isActivityRunning;

    private int steps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        setTitle("Step Counter");

        pedometerTextView = (TextView) findViewById(R.id.pedometer_value_textView);
        noteStepsTextView = (TextView) findViewById(R.id.noteSteps_textview);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {

        super.onResume();
        isActivityRunning = true;

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(countSensor != null) {

            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {

            Toast.makeText(this, "Count sensor not detected", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityRunning = false;

        // sensorManager.unregisterListener(this); // stop the hardware from detecting steps
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (isActivityRunning) {

            // set steps
            String currStep = String.valueOf(event.values[0]);
            pedometerTextView.setText(currStep);
            steps = (int)Math.floor(Double.parseDouble(currStep));

            // set note
            int remainingSteps = DAILY_STEPS_LIMIT - steps;

            if(remainingSteps < 0) {
                remainingSteps = 0;
            }

            noteStepsTextView.setText(remainingSteps + NOTE_DAILY_STEPS);

            // update new steps in database
            Calendar currCal = Calendar.getInstance();
            currCal.set(Calendar.HOUR_OF_DAY, 0);
            currCal.set(Calendar.MINUTE, 0);
            currCal.set(Calendar.SECOND, 0);
            currCal.set(Calendar.MILLISECOND, 0);

            DatabaseHandler db = new DatabaseHandler(this);
            db.addStepsValues(currCal, steps);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // do nothing
    }
}
