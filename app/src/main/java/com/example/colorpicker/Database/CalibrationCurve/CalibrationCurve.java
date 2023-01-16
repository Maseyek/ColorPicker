package com.example.colorpicker.Database.CalibrationCurve;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "calibration_curve")
public class CalibrationCurve {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "Name")
    public String Name;
}
