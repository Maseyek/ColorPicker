package com.example.colorpicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.Database.ColorMeasurement.ColorMeasurement;
import com.example.colorpicker.Database.ColorMeasurement.ColorMeasurementDao;
import com.example.colorpicker.Database.ColorMeasurement.Measurement;
import com.example.colorpicker.Database.ColorMeasurement.MeasurementAdapter;
import com.example.colorpicker.Database.ColorMeasurement.MeasurementDao;

import java.util.List;

public class MeasurementActivity extends AppCompatActivity {

    public Button NewMeasurement;
    private List<Measurement> measurements;
    private boolean showResults;
    private ColorMeasurement colorMeasurement;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        NewMeasurement = findViewById(R.id.newMeasurement);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        MeasurementDao dao = db.measurementDao();
        ColorMeasurementDao colorMeasurementDao = db.colorMeasurementDao();
        measurements = dao.getAll();

        //Check if this activity was opened with button located in MainActivity's toolbar
        showResults = getIntent().getBooleanExtra("showResults", false);
        if(showResults){
            //if yes, then disable 'NewMeasurement' button and create an empty colorMeasurement (needed for RecyclerViewAdapter)
            colorMeasurement = new ColorMeasurement();
            colorMeasurement.R = -1;
            NewMeasurement.setVisibility(View.INVISIBLE);
        }
        else{
            colorMeasurement = (ColorMeasurement) getIntent().getSerializableExtra("ColorMeasurement");
        }

        MeasurementAdapter adapter = new MeasurementAdapter(measurements, colorMeasurement, colorMeasurementDao, MeasurementActivity.this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        EditText inputView = new EditText(this);
        // Set the input type to text
        inputView.setInputType(InputType.TYPE_CLASS_TEXT);
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Enter Name of Measurement")
                .setView(inputView)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Get the text from the input view
                    String inputText = inputView.getText().toString();
                    Measurement measurement = new Measurement();
                    measurement.Name = inputText;
                    int id = (int)dao.insert(measurement);
                    measurements.add(measurement);

                    colorMeasurement.measurement_id = id;
                    colorMeasurementDao.insert(colorMeasurement);
                    Intent intent = new Intent( this, ColorMeasurementActivity.class);
                    intent.putExtra("measurementId", id);
                    startActivity(intent);
                });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        NewMeasurement.setOnClickListener(view -> {
            if(inputView.getParent() != null) {
                ((ViewGroup)inputView.getParent()).removeView(inputView); // <- fix
            }
            builder.show();
        });


    }


}