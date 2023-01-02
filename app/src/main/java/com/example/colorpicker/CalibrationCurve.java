package com.example.colorpicker;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CalibrationCurve extends AppCompatActivity {

    public Button CalculateCurve, CalculateX;
    public EditText inputR, inputG, inputB, inputCon, inputM, inputC, inputY;
    public TextView result, resultX;
    public ImageButton Confirm;

    ArrayList<Integer> valuesCon = new ArrayList<>();
    ArrayList<Integer> valuesR = new ArrayList<>();
    ArrayList<Integer> valuesG = new ArrayList<>();
    ArrayList<Integer> valuesB = new ArrayList<>();

    double m, c, r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration_curve);

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


        Confirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String valueR = inputR.getText().toString();
                String valueG = inputG.getText().toString();
                String valueB = inputB.getText().toString();
                String valueCon = inputCon.getText().toString();
                valuesR.add(Integer.parseInt(valueR));
                valuesG.add(Integer.parseInt(valueG));
                valuesB.add(Integer.parseInt(valueB));
                valuesCon.add(Integer.parseInt(valueCon));



                Toast.makeText(CalibrationCurve.this, "Value added", Toast.LENGTH_LONG).show();

            }


        });

        CalculateCurve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] ArrayCon = valuesCon.stream().mapToInt(i -> i).toArray();
                int[] ArrayR = valuesR.stream().mapToInt(i -> i).toArray();


                bestApprox(ArrayCon, ArrayR);
                result.setText("R^2 = "+ r +" m = " + m + " c = " + c);

            }
        });

        CalculateX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double valueY, valueM, valueC, valueX;
                String text = inputY.getText().toString();
                valueY = Double.parseDouble(text);
                text = inputM.getText().toString();
                valueM = Double.parseDouble(text);
                text = inputC.getText().toString();
                valueC = Double.parseDouble(text);

                valueX = (valueY - valueC) / valueM;

                resultX.setText("X = "+ valueX);

            }
        });

    }


    public void bestApprox(int x[], int y[]){
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