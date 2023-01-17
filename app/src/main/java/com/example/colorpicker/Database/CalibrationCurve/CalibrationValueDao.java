package com.example.colorpicker.Database.CalibrationCurve;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalibrationValueDao {
    @Insert
    void insert(CalibrationValue calibrationValue);

    @Update
    void update(CalibrationValue calibrationValue);

    @Delete
    void delete(CalibrationValue calibrationValue);

    @Query("SELECT * FROM calibration_values WHERE calibration_curve_id = :calibrationCurveId")
    List<CalibrationValue> getCalibrationValuesByCurveId(int calibrationCurveId);
}

