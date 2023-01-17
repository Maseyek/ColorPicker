package com.example.colorpicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurve;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurveAdapter;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurveDao;

import java.util.List;

public class CalibrationCurveListActivity extends AppCompatActivity {

    public Button NewCalibrationCurve;
    private List<CalibrationCurve> calibrationCurveList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration_curve_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        NewCalibrationCurve = findViewById(R.id.newCurve);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        CalibrationCurveDao dao = db.calibrationCurveDao();

        calibrationCurveList = dao.getAll();

        CalibrationCurveAdapter adapter = new CalibrationCurveAdapter(calibrationCurveList, dao, CalibrationCurveListActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText inputView = new EditText(this);
        // Set the input type to text
        inputView.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter Name of Calibration Curve")
                .setView(inputView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Get the text from the input view
                    String inputText = inputView.getText().toString();
                    CalibrationCurve measurement = new CalibrationCurve();
                    measurement.Name = inputText;
                    int id = (int)dao.insert(measurement);
                    calibrationCurveList.add(measurement);

                    Intent intent = new Intent( this, CalibrationCurveActivity.class);
                    intent.putExtra("calibrationCurveId", id);
                    startActivity(intent);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        NewCalibrationCurve.setOnClickListener(view -> {
            if(inputView.getParent() != null) {
                ((ViewGroup)inputView.getParent()).removeView(inputView); // <- fix
            }
            builder.show();
        });
    }
}