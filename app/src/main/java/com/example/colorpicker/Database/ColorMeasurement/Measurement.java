package com.example.colorpicker.Database.ColorMeasurement;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "measurements")
public class Measurement {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Name")
    public String Name;
}
