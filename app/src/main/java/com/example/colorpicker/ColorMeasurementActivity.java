package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

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

        int measurementId = getIntent().getIntExtra("measurementId", 0);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ColorMeasurementDao colorMeasurementDao = db.colorMeasurementDao();
        colorMeasurements = colorMeasurementDao.getByMeasurementId(measurementId);




        // Inflate the layout for the pop-up window
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);


        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, focusable);

        // Set the background of the pop-up window to the desired color
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE)); // red color

        // Dismiss the pop-up window when the user taps outside of it
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });


        ColorMeasurementAdapter adapter = new ColorMeasurementAdapter(colorMeasurements, popupWindow, findViewById(android.R.id.content));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        popupView.findViewById(R.id.btnCancel).setOnClickListener(view -> {
            popupWindow.dismiss();
        });



    }
}