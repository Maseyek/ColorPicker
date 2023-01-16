package com.example.colorpicker.Database.CalibrationCurve;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpicker.R;

import java.util.List;

public class CalibrationValueAdapter extends RecyclerView.Adapter<CalibrationValueAdapter.ViewHolder> {

    private List<CalibrationValue> calibrationValues;

    public CalibrationValueAdapter(List<CalibrationValue> calibrationValues) {
        this.calibrationValues = calibrationValues;
    }

    @Override
    public CalibrationValueAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.value_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalibrationValueAdapter.ViewHolder holder, int position) {
        CalibrationValue calibrationValue = calibrationValues.get(position);
        holder.tvR.setText(String.valueOf(calibrationValue.R));
        holder.tvG.setText(String.valueOf(calibrationValue.G));
        holder.tvB.setText(String.valueOf(calibrationValue.B));
        holder.tvConcentration.setText(String.valueOf(calibrationValue.Concentration));
    }
    @Override
    public int getItemCount() {
        return calibrationValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvR;
        TextView tvG;
        TextView tvB;
        TextView tvConcentration;

        public ViewHolder(View itemView) {
            super(itemView);
            tvR = itemView.findViewById(R.id.tv_r);
            tvG = itemView.findViewById(R.id.tv_g);
            tvB = itemView.findViewById(R.id.tv_b);
            tvConcentration = itemView.findViewById(R.id.tv_concentration);
        }
    }

}

