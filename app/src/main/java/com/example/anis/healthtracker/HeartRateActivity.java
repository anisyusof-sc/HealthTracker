package com.example.anis.healthtracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HeartRateActivity extends AppCompatActivity {

    private Calendar globalCal;
    private EditText hrEditText;
    private EditText systolicEditText;
    private EditText diastolicEditText;
    private EditText dateEditText;
    private EditText timeEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        setTitle("Heart Rate Monitor");
        globalCal = Calendar.getInstance();

        hrEditText = (EditText) findViewById(R.id.hr_edittext);
        systolicEditText = (EditText) findViewById(R.id.systolic_edittext);
        diastolicEditText = (EditText) findViewById(R.id.diastolic_edittext);
        dateEditText = (EditText) findViewById(R.id.hr_date_edittext);
        timeEditText = (EditText) findViewById(R.id.hr_time_edittext);
        saveButton = (Button) findViewById(R.id.hr_save_btn);

        setDateTimeToday();
        setListener();
    }

    private void setDateTimeToday() {

        updateDateText(globalCal);
        updateTimeText(globalCal);
    }

    private void setListener() {

        // date picker
        dateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                DatePickerDialog mdatePicker = new DatePickerDialog(HeartRateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mcurrentTime.set(Calendar.YEAR, year);
                        mcurrentTime.set(Calendar.MONTH, monthOfYear);
                        mcurrentTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateText(mcurrentTime);
                    }
                }, mcurrentTime.get(Calendar.YEAR), mcurrentTime.get(Calendar.MONTH),
                        mcurrentTime.get(Calendar.DAY_OF_MONTH));

                mdatePicker.setTitle("Select Date");
                mdatePicker.show();
            }
        });

        // time picker
        timeEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(HeartRateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeEditText.setText( selectedHour + ":" + selectedMinute);
                        mcurrentTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        mcurrentTime.set(Calendar.MINUTE, selectedMinute);
                        updateTimeText(mcurrentTime);
                    }
                }, hour, minute, true);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                saveValues();
                Toast.makeText(getApplicationContext(),
                        "Information saved successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HeartRateActivity.this, MainActivity.class));
            }
        });
    }

    private void updateDateText(Calendar cal) {

        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdfDate = new SimpleDateFormat(dateFormat, Locale.US);

        dateEditText.setText(sdfDate.format(cal.getTime()));
    }

    private void updateTimeText(Calendar cal) {

        String timeFormat = "HH:mm";
        SimpleDateFormat sdfTime = new SimpleDateFormat(timeFormat, Locale.US);

        timeEditText.setText(sdfTime.format(cal.getTime()));
    }

    private void saveValues() {

        DatabaseHandler db = new DatabaseHandler(this);

        String dateTimeFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdfDateTime = new SimpleDateFormat(dateTimeFormat, Locale.US);

        String dateTime = dateEditText.getText().toString() + " "
                +timeEditText.getText().toString();

        Calendar newCal = Calendar.getInstance();

        try {
            newCal.setTime(sdfDateTime.parse(dateTime));
        }
        catch(Exception e) {
            // exception
        }

        int hr = Integer.parseInt(hrEditText.getText().toString());
        int systolic = Integer.parseInt(systolicEditText.getText().toString());
        int diastolic = Integer.parseInt(diastolicEditText.getText().toString());
        db.addHrValues(hr, systolic, diastolic, newCal);
    }
}
