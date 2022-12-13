package com.example.colorpicker.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Measurement.class, ColorMeasurement.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MeasurementDao measurementDao();
    public abstract ColorMeasurementDao colorMeasurementDao();
}

