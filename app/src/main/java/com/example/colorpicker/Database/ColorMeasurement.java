package com.example.colorpicker.Database;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "color_measurements",
        foreignKeys = @ForeignKey(entity = Measurement.class,
                parentColumns = "id",
                childColumns = "measurement_id",
                onDelete = CASCADE))
public class ColorMeasurement {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "R")
    public int R;

    @ColumnInfo(name = "G")
    public int G;

    @ColumnInfo(name = "B")
    public int B;
    @ColumnInfo(name = "Date")
    public Date Date;

    public int measurement_id;
}
