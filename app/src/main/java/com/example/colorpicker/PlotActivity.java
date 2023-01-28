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

    int[] redValues;
    double[] redResults;
    int[] greenValues;
    double[] greenResults;
    int[] blueValues;
    double[] blueResults;
    int[] sumValues;
    double[] sumResults;
    int[] concentration;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);
        tabs = findViewById(R.id.tabs);
        tv1 = findViewById(R.id.PlotTextView1);
        graph = (GraphView) findViewById(R.id.graph);

        redValues = getIntent().getIntArrayExtra("redValues");
        redResults = getIntent().getDoubleArrayExtra("redResults");
        greenValues = getIntent().getIntArrayExtra("greenValues");
        greenResults = getIntent().getDoubleArrayExtra("greenResults");
        blueValues = getIntent().getIntArrayExtra("blueValues");
        blueResults = getIntent().getDoubleArrayExtra("blueResults");
        sumValues = getIntent().getIntArrayExtra("sumValues");
        sumResults = getIntent().getDoubleArrayExtra("sumResults");
        concentration = getIntent().getIntArrayExtra("concentration");

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

    void showPlot(int[] points, double[] parameters, int color) {
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
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        if(color == Color.BLACK)
            graph.getViewport().setMaxX(max);
        else
            graph.getViewport().setMaxX(max);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(100);

        //Sorting the values (needed for plotting...)
        //Hashtable <key=ColorValue, value=Concentration>
        Hashtable<Integer, Integer> values = new Hashtable<>();
        for(int i=0; i<points.length; i++){
            values.put(points[i], concentration[i]);
        }
        List sortedKeys = new ArrayList(values.keySet());
        Collections.sort(sortedKeys);
        DataPoint[] dataPoints = new DataPoint[sortedKeys.size()];
        for(int i=0; i< sortedKeys.size(); i++){
            int x = (int) sortedKeys.get(i);
            int y = values.get(x);
            dataPoints[i] = new DataPoint(x, y);
        }

        // Plotting the input color values and corresponding concentrations
        PointsGraphSeries<DataPoint> inputValues = new PointsGraphSeries<>(dataPoints);
        graph.addSeries(inputValues);
        inputValues.setColor(color);
        inputValues.setShape(PointsGraphSeries.Shape.POINT);


        //y = mx + c -> x = (y-c)/m
        dataPoints = new DataPoint[max+1];
        for (int i = 0; i <= max; i++) {
            double x = (double) i;
            int y = (int) (m * x + c);
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