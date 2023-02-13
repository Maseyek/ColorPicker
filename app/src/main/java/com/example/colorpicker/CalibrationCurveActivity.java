package com.example.colorpicker;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurve;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurveDao;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationValue;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationValueAdapter;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationValueDao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalibrationCurveActivity extends AppCompatActivity {

    public Button CalculateCurve, CalculateX, PlotCalibrationCurves;
    public EditText inputR, inputG, inputB, inputCon, inputM, inputC, inputY;
    public TextView result, resultX;
    public ImageButton Confirm;

    double m, c, r;
    int calibrationCurveId;
    DecimalFormat df =new DecimalFormat("#.###");

    CalibrationValueAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration_curve);

        calibrationCurveId = getIntent().getIntExtra("calibrationCurveId", 0);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        CalibrationValueDao dao = db.calibrationValueDao();


        List<CalibrationValue> calibrationValues = dao.getCalibrationValuesByCurveId(calibrationCurveId);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CalibrationValueAdapter(calibrationValues, dao);
        recyclerView.setAdapter(adapter);

        result = findViewById(R.id.resultR);
        resultX = findViewById(R.id.resultX);

        inputR = findViewById(R.id.editTextNumberR);
        inputG = findViewById(R.id.editTextNumberG);
        inputB = findViewById(R.id.editTextNumberB);
        inputCon = findViewById(R.id.editTextNumberCon);
        inputM = findViewById(R.id.editTextNumberM);
        inputC = findViewById(R.id.editTextNumberC);
        inputY = findViewById(R.id.editTextNumberY);
        Confirm = findViewById(R.id.confirm_button);
        CalculateCurve = findViewById(R.id.Calculate);
        CalculateX = findViewById(R.id.CalculateX);
        PlotCalibrationCurves = findViewById(R.id.PlotCalibrationCurves);


        Confirm.setOnClickListener(view -> {
            String valueR = inputR.getText().toString();
            String valueG = inputG.getText().toString();
            String valueB = inputB.getText().toString();
            String valueCon = inputCon.getText().toString();
            if(!(valueR.isEmpty() || valueG.isEmpty() || valueB.isEmpty() || valueCon.isEmpty())) {
                CalibrationValue calibrationValue = new CalibrationValue();
                calibrationValue.calibrationCurveId = calibrationCurveId;
                calibrationValue.R = Double.parseDouble(valueR);
                calibrationValue.R = Double.parseDouble(df.format(calibrationValue.R));
                calibrationValue.G = Double.parseDouble(valueG);
                calibrationValue.G = Double.parseDouble(df.format(calibrationValue.G));
                calibrationValue.B = Double.parseDouble(valueB);
                calibrationValue.B = Double.parseDouble(df.format(calibrationValue.B));
                calibrationValue.Concentration = Double.parseDouble(valueCon);
                dao.insert(calibrationValue);
                // Get the position of the newly added item
                int position = calibrationValues.size();

                adapter.notifyItemInserted(position);
                Toast.makeText(CalibrationCurveActivity.this, "Value added", Toast.LENGTH_LONG).show();
                recreate();
            }
            else
                Toast.makeText(CalibrationCurveActivity.this, "You cannot get water out of a stone. " +
                                                                        "\nFill all the values", Toast.LENGTH_LONG).show();
    });

        CalculateCurve.setOnClickListener(view -> {

            if(calibrationValues.size() < 2)
                Toast.makeText(CalibrationCurveActivity.this, "Not enough points to proceed", Toast.LENGTH_LONG).show();
            else {
                double[] ArrayCon = calibrationValues.stream().mapToDouble(x -> x.Concentration).toArray();
                double[] ArrayR = calibrationValues.stream().mapToDouble(x -> x.R).toArray();
                double[] ArrayG = calibrationValues.stream().mapToDouble(x -> x.G).toArray();
                double[] ArrayB = calibrationValues.stream().mapToDouble(x -> x.B).toArray();
                double[] sum = new double[ArrayB.length];
                for (int i = 0; i < ArrayB.length; i++)
                    sum[i] = ArrayR[i] + ArrayG[i] + ArrayB[i];


                bestApprox(ArrayCon, ArrayR);
                result.setText("R: " + String.format("R^2 = %.4f", r) + String.format(" m = %.3f", m) + String.format(" c = %.3f", c));
                bestApprox(ArrayCon, ArrayG);
                result.append("\n G: " + String.format("R^2 = %.4f", r) + String.format(" m = %.3f", m) + String.format(" c = %.3f", c));
                bestApprox(ArrayCon, ArrayB);
                result.append("\n B: " + String.format("R^2 = %.4f", r) + String.format(" m = %.3f", m) + String.format(" c = %.3f", c));
                bestApprox(ArrayCon, sum);
                result.append("\n Sum: " + String.format("R^2 = %.4f", r) + String.format(" m = %.3f", m) + String.format(" c = %.3f", c));
            }
        });

        CalculateX.setOnClickListener(view -> {
            double valueY, valueM, valueC, valueX;
            String text = inputY.getText().toString();
            valueY = Double.parseDouble(text);
            text = inputM.getText().toString();
            valueM = Double.parseDouble(text);
            text = inputC.getText().toString();
            valueC = Double.parseDouble(text);

            valueX = (valueY - valueC) / valueM;

            resultX.setText("X = "+ valueX);

        });

        PlotCalibrationCurves.setOnClickListener(view -> {
            if (calibrationValues.size() < 2)
            {
                Toast.makeText(CalibrationCurveActivity.this, "Not enough points to proceed", Toast.LENGTH_LONG).show();
                return;
            }
            double[] ArrayCon = calibrationValues.stream().mapToDouble(x -> x.Concentration).toArray();
            double[] ArrayR = calibrationValues.stream().mapToDouble(x -> x.R).toArray();
            double[] ArrayG = calibrationValues.stream().mapToDouble(x -> x.G).toArray();
            double[] ArrayB = calibrationValues.stream().mapToDouble(x -> x.B).toArray();
            double[] sum = new double[ArrayB.length];
            for (int i = 0; i < ArrayB.length; i++)
                sum[i] = ArrayR[i] + ArrayG[i] + ArrayB[i];

            bestApprox(ArrayCon, ArrayR);
            double[] redResults = {m, c, r};
            bestApprox(ArrayCon, ArrayG);
            double[] greenResults = {m, c, r};
            bestApprox(ArrayCon, ArrayB);
            double[] blueResults = {m, c, r};
            bestApprox(ArrayCon, sum);
            double[] sumResults = {m, c, r};
            Intent intent = new Intent(this, PlotActivity.class);
            intent.putExtra("redValues", ArrayR);
            intent.putExtra("redResults", redResults);
            intent.putExtra("greenValues", ArrayG);
            intent.putExtra("greenResults", greenResults);
            intent.putExtra("blueValues", ArrayB);
            intent.putExtra("blueResults", blueResults);
            intent.putExtra("sumValues", sum);
            intent.putExtra("sumResults", sumResults);
            intent.putExtra("concentration", ArrayCon);
            startActivity(intent);
        });
    }


    public void bestApprox(double x[], double y[]){
        int n = x.length;
        m = 0;
        c = 0;
        r = 0;
        double temp = 0, sum_x = 0, sum_y = 0,
                sum_xy = 0, sum_x2 = 0,
                sum_y2 = 0;
        for (int i = 0; i < n; i++) {
            sum_x += x[i];
            sum_y += y[i];
            sum_xy += x[i] * y[i];
            sum_x2 += pow(x[i], 2);
            sum_y2 += pow(y[i], 2);
        }

        m = (n * sum_xy - sum_x * sum_y) / (n * sum_x2 - pow(sum_x, 2));
        c = (sum_y - m * sum_x) / n;
        temp = (n * sum_xy - sum_x*sum_y)/(sqrt((n * sum_x2 - pow(sum_x,2))*(n * sum_y2 - pow(sum_y,2))));
        r = pow(temp, 2);

    }



}