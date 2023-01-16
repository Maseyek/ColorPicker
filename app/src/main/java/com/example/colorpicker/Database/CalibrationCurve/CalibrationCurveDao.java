package com.example.colorpicker.Database.CalibrationCurve;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalibrationCurveDao {
    @Insert
    long insert(CalibrationCurve calibrationCurve);

    @Update
    void update(CalibrationCurve calibrationCurve);

    @Delete
    void delete(CalibrationCurve calibrationCurve);

    @Query("SELECT * FROM calibration_curve")
    List<CalibrationCurve> getAll();

    @Query("SELECT * FROM calibration_curve WHERE id = :id")
    CalibrationCurve getById(long id);
}
