package com.example.anis.healthtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private GraphView stepsBarGraph;
    private GraphView hrBarGraph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        setTitle("Your Past Activity");

        stepsBarGraph = (GraphView) findViewById(R.id.steps_graph);
        hrBarGraph = (GraphView) findViewById(R.id.hr_graph);

        drawStepsGraph();
        drawHrGraph();
    }

    private void drawStepsGraph() {

        DatabaseHandler db = new DatabaseHandler(this);
        List<Integer> stepsList = db.getSteps();
        DataPoint[] pointValues = new DataPoint[stepsList.size()];

        for(int i = 0; i < stepsList.size(); i++) {

            DataPoint currPoint = new DataPoint(i, stepsList.get(i));
            pointValues[i] = currPoint;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(pointValues);
        stepsBarGraph.addSeries(series);
    }

    private void drawHrGraph() {

        DatabaseHandler db = new DatabaseHandler(this);
        List<Integer> hrList = db.getHr();
        DataPoint[] pointValues = new DataPoint[hrList.size()];

        for(int i = 0; i < hrList.size(); i++) {

            DataPoint currPoint = new DataPoint(i, hrList.get(i));
            pointValues[i] = currPoint;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(pointValues);
        hrBarGraph.addSeries(series);
    }
}
