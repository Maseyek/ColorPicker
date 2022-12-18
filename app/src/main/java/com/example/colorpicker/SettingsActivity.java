package com.example.colorpicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;



public class SettingsActivity extends AppCompatActivity {

    int precision;
    SeekBar precisionSeekBar;
    TextView precisionTextView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        precision = getIntent().getIntExtra("precision", 10);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Settings");

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        precisionSeekBar = (SeekBar) findViewById(R.id.precisionSeekBar);
        precisionSeekBar.setMax(10);
        precisionSeekBar.setMin(1);
        precisionSeekBar.setProgress(precisionSeekBar.getMax() - precision/2 + 1);

        precisionTextView = (TextView) findViewById(R.id.currentPrecision);
        precisionTextView.setText("Current precision: " + precision);

        precisionSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                precision = 2*(precisionSeekBar.getMax() - i + 1);
                precisionTextView.setText("Current precision: " + precision);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent data = new Intent();
                //---set the data to pass back---
                data.setData(Uri.parse(String.valueOf(precision)));
                setResult(RESULT_OK, data);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}