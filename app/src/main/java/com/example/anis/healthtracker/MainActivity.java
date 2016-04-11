package com.example.anis.healthtracker;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.ImageButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ImageButton heartRateButton;
    private ImageButton stepButton;
    private ImageButton sleepButton;
    private ImageButton graphButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Health Tracker");

        heartRateButton = (ImageButton)findViewById(R.id.heart_btn);
        stepButton = (ImageButton)findViewById(R.id.step_btn);
        sleepButton = (ImageButton)findViewById(R.id.sleep_btn);
        graphButton = (ImageButton)findViewById(R.id.graph_btn);

        setListener();
        addDummyData();
    }

    private void setListener() {

        heartRateButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, HeartRateActivity.class));
            }
        });

        stepButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, StepActivity.class));
            }
        });

        graphButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, GraphActivity.class));
            }
        });
    }

    private void addDummyData() {

        DatabaseHandler db = new DatabaseHandler(this);

        // yesterday
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, -1);
        db.addStepsValues(cal1, 2533);

        // 2 days ago
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, -2);
        db.addStepsValues(cal2, 3122);

        // 3 days ago
        Calendar cal3 = Calendar.getInstance();
        cal3.add(Calendar.DAY_OF_MONTH, -3);
        db.addStepsValues(cal3, 2799);
    }
}
