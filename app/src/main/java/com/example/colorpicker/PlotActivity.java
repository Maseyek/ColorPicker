package com.example.colorpicker;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class PlotActivity extends AppCompatActivity {

    TabLayout tabs;
    int selectedTab;
    TextView tv1;

    double[] redValues;
    double[] redResults;
    double[] greenValues;
    double[] greenResults;
    double[] blueValues;
    double[] blueResults;
    double[] sumValues;
    double[] sumResults;
    double[] concentration;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        tabs = findViewById(R.id.tabs);
        tv1 = findViewById(R.id.PlotTextView1);
        graph = (GraphView) findViewById(R.id.graph);

        redValues = getIntent().getDoubleArrayExtra("redValues");
        redResults = getIntent().getDoubleArrayExtra("redResults");
        greenValues = getIntent().getDoubleArrayExtra("greenValues");
        greenResults = getIntent().getDoubleArrayExtra("greenResults");
        blueValues = getIntent().getDoubleArrayExtra("blueValues");
        blueResults = getIntent().getDoubleArrayExtra("blueResults");
        sumValues = getIntent().getDoubleArrayExtra("sumValues");
        sumResults = getIntent().getDoubleArrayExtra("sumResults");
        concentration = getIntent().getDoubleArrayExtra("concentration");

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTab = tabs.getSelectedTabPosition();
                switch(selectedTab) {
                    case 0:
                        showPlot(redValues, redResults, Color.RED);
                        break;
                    case 1:
                        showPlot(greenValues, greenResults, Color.GREEN);
                        break;
                    case 2:
                        showPlot(blueValues, blueResults, Color.BLUE);
                        break;
                    case 3:
                        showPlot(sumValues, sumResults, Color.BLACK);
                        break;
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //debug();
            }
        });

        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        showPlot(redValues, redResults, Color.RED);
    }

    void showPlot(double[] points, double[] parameters, int color) {
        double m, c, r;
        m = parameters[0];
        c = parameters[1];
        r = parameters[2];
        tv1.setText(String.format("R^2 = %.4f", r) + String.format(" m = %.3f", m) + String.format(" c = %.3f", c));
        graph.removeAllSeries();

        int max = 255;
        if(color == Color.BLACK)
            max = 255*3;

        // set manual X bounds
        int MinX = (int)Arrays.stream(concentration).min().getAsDouble();
        int MaxX = (int)Arrays.stream(concentration).max().getAsDouble();
        graph.getViewport().setXAxisBoundsManual(true);
        if (MinX < 50)
            graph.getViewport().setMinX(0);
        else
            graph.getViewport().setMinX(MinX-50);

        graph.getViewport().setMaxX(MaxX+50);

        // set manual Y bounds
        int MaxY = (int) Arrays.stream(points).max().getAsDouble();
        int MinY = (int) Arrays.stream(points).min().getAsDouble();
        graph.getViewport().setYAxisBoundsManual(true);
        if (MinY < 25)
            graph.getViewport().setMinY(0);
        else
            graph.getViewport().setMinY(MinY-25);

        graph.getViewport().setMaxY(MaxY+25);


        //Sorting the values (needed for plotting...)
        //Hashtable <key=ColorValue, value=Concentration>
        Hashtable<Double, Double> values = new Hashtable<>();
        for(int i=0; i<points.length; i++){
            values.put(concentration[i],points[i]);
        }
        List sortedKeys = new ArrayList(values.keySet());
        Collections.sort(sortedKeys);
        DataPoint[] dataPoints = new DataPoint[sortedKeys.size()];
        for(int i=0; i< sortedKeys.size(); i++){
            double x = (double) sortedKeys.get(i);
            double y = values.get(x);
            dataPoints[i] = new DataPoint(x, y);
        }

        // Plotting the input color values and corresponding concentrations
        PointsGraphSeries<DataPoint> inputValues = new PointsGraphSeries<>(dataPoints);
        graph.addSeries(inputValues);
        inputValues.setColor(color);
        inputValues.setShape(PointsGraphSeries.Shape.POINT);

        graph.getGridLabelRenderer().setHorizontalAxisTitle("Concentration [mg/L]");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Values");


        //y = mx + c -> x = (y-c)/m
        dataPoints = new DataPoint[max+1];
        for (int i = 0; i <= max; i++) {
            double x = (double) i;
            double y = (m*x + c);
            dataPoints[i] = new DataPoint(i, y);
        }
        LineGraphSeries<DataPoint> lineApproximation = new LineGraphSeries<>(dataPoints);
        graph.addSeries(lineApproximation);
    }


    public static int maxValue(final int[] intArray) {
        return IntStream.range(0, intArray.length).map(i -> intArray[i]).max().getAsInt();
    }

//    void debug()
//    {
//        Log.d("myTag", "Red:");
//        Log.d("myTag", Arrays.toString(redValues));
//        Log.d("myTag", Arrays.toString(redResults));
//        Log.d("myTag", "Green:");
//        Log.d("myTag", Arrays.toString(greenValues));
//        Log.d("myTag", Arrays.toString(greenResults));
//        Log.d("myTag", "Blue:");
//        Log.d("myTag", Arrays.toString(blueValues));
//        Log.d("myTag", Arrays.toString(blueResults));
//        Log.d("myTag", "Sum:");
//        Log.d("myTag", Arrays.toString(sumValues));
//        Log.d("myTag", Arrays.toString(sumResults));
//    }
}