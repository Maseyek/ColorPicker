package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;

import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.Database.ColorMeasurement;
import com.example.colorpicker.Database.ColorMeasurementAdapter;
import com.example.colorpicker.Database.ColorMeasurementDao;
import com.example.colorpicker.Database.Measurement;
import com.example.colorpicker.Database.MeasurementAdapter;
import com.example.colorpicker.Database.MeasurementDao;

import java.util.List;

public class ColorMeasurementActivity extends AppCompatActivity {

    private List<ColorMeasurement> colorMeasurements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_measurement);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        Intent intent = getIntent();
        int measurementId = intent.getIntExtra("measurementId", 0);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ColorMeasurementDao colorMeasurementDao = db.colorMeasurementDao();
        colorMeasurements = colorMeasurementDao.getByMeasurementId(measurementId);


        ColorMeasurementAdapter adapter = new ColorMeasurementAdapter(colorMeasurements);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}