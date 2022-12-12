package com.example.colorpicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.Database.Measurement;
import com.example.colorpicker.Database.MeasurementAdapter;
import com.example.colorpicker.Database.MeasurementDao;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MeasurementActivity extends AppCompatActivity {

    public Button NewMeasurement;
    private List<Measurement> measurements;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement);
        NewMeasurement = findViewById(R.id.newMeasurement);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        MeasurementDao dao = db.measurementDao();
        measurements = dao.getAll();

        MeasurementAdapter adapter = new MeasurementAdapter(measurements);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        int[] array = intent.getIntArrayExtra("array");

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
                    dao.insert(measurement);
                    measurements.add(measurement);
                    adapter.notifyItemInserted(measurements.size()-1);
                    inputText = "";
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