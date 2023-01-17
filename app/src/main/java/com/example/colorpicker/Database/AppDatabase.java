package com.example.colorpicker.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurve;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurveDao;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationValue;
import com.example.colorpicker.Database.CalibrationCurve.CalibrationValueDao;
import com.example.colorpicker.Database.ColorMeasurement.ColorMeasurement;
import com.example.colorpicker.Database.ColorMeasurement.ColorMeasurementDao;
import com.example.colorpicker.Database.ColorMeasurement.Converters;
import com.example.colorpicker.Database.ColorMeasurement.Measurement;
import com.example.colorpicker.Database.ColorMeasurement.MeasurementDao;

@Database(entities = {Measurement.class, ColorMeasurement.class, CalibrationCurve.class, CalibrationValue.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeasurementDao measurementDao();
    public abstract ColorMeasurementDao colorMeasurementDao();
    public abstract CalibrationCurveDao calibrationCurveDao();
    public abstract CalibrationValueDao calibrationValueDao();
}

