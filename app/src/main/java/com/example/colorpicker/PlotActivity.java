package com.example.colorpicker;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.colorpicker.ui.main.SectionsPagerAdapter;
import com.example.colorpicker.databinding.ActivityPlotBinding;

public class PlotActivity extends AppCompatActivity {

    private ActivityPlotBinding binding;
    int[] redValues;
    int[] redResults;
    int[] greenValues;
    int[] greenResults;
    int[] blueValues;
    int[] blueResults;
    int[] sumValues;
    int[] sumResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        redValues = getIntent().getIntArrayExtra("redValues");
        redResults = getIntent().getIntArrayExtra("redResults");
        greenValues = getIntent().getIntArrayExtra("greenValues");
        greenResults = getIntent().getIntArrayExtra("greenResults");
        blueValues = getIntent().getIntArrayExtra("blueValues");
        blueResults = getIntent().getIntArrayExtra("blueResults");
        sumValues = getIntent().getIntArrayExtra("sumValues");
        sumResults = getIntent().getIntArrayExtra("sumResults");

        binding = ActivityPlotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}