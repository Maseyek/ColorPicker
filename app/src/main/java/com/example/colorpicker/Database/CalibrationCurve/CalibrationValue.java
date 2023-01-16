package com.example.colorpicker.Database.CalibrationCurve;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.colorpicker.Database.CalibrationCurve.CalibrationCurve;

@Entity(tableName = "calibration_values",
        foreignKeys = @ForeignKey(entity = CalibrationCurve.class,
                parentColumns = "id",
                childColumns = "calibration_curve_id",
                onDelete = CASCADE))
public class CalibrationValue {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "R")
    public int R;
    @ColumnInfo(name = "G")
    public int G;
    @ColumnInfo(name = "B")
    public int B;
    @ColumnInfo(name = "Concentration")
    public int Concentration;
    @ColumnInfo(name = "calibration_curve_id")
    public int calibrationCurveId;
}
