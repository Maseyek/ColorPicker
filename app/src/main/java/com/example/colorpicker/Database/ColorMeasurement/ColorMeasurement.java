package com.example.colorpicker.Database.ColorMeasurement;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "color_measurements",
        foreignKeys = @ForeignKey(entity = Measurement.class,
                parentColumns = "id",
                childColumns = "measurement_id",
                onDelete = CASCADE))
public class ColorMeasurement implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "R")
    public int R;

    @ColumnInfo(name = "G")
    public int G;

    @ColumnInfo(name = "B")
    public int B;

    @ColumnInfo(name = "RMax")
    public int RMax;

    @ColumnInfo(name = "GMax")
    public int GMax;

    @ColumnInfo(name = "BMax")
    public int BMax;
    @ColumnInfo(name = "RMin")
    public int RMin;

    @ColumnInfo(name = "GMin")
    public int GMin;

    @ColumnInfo(name = "BMin")
    public int BMin;

    @ColumnInfo(name = "RMedian")
    public int RMedian;

    @ColumnInfo(name = "GMedian")
    public int GMedian;

    @ColumnInfo(name = "BMedian")
    public int BMedian;

    @ColumnInfo(name = "Date")
    public Date Date;

    public Integer measurement_id = 0;
}
