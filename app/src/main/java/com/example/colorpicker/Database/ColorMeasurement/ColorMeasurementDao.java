package com.example.colorpicker.Database.ColorMeasurement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
// Interfejs zawierający metody do zapisywania, aktualizowania, usuwania i wyszukiwania danych w bazie danych
public interface ColorMeasurementDao {
    @Insert
    void insert(ColorMeasurement measurement);

    @Update
    void update(ColorMeasurement measurement);

    @Delete
    void delete(ColorMeasurement measurement);

    @Query("SELECT * FROM color_measurements WHERE measurement_id = :measurementId")
    List<ColorMeasurement> getByMeasurementId(int measurementId);
}