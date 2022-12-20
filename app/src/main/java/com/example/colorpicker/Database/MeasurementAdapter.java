package com.example.colorpicker.Database;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpicker.ColorMeasurementActivity;
import com.example.colorpicker.MainActivity;
import com.example.colorpicker.MeasurementActivity;
import com.example.colorpicker.R;

import java.util.List;

public class MeasurementAdapter extends RecyclerView.Adapter<MeasurementAdapter.ViewHolder> {
    private List<Measurement> entities;
    private ColorMeasurement colorMeasurement;
    private ColorMeasurementDao colorMeasurementDao;
    private Context context;

    public MeasurementAdapter(List<Measurement> entities, ColorMeasurement colorMeasurement, ColorMeasurementDao colorMeasurementDao, Context context) {
        this.entities = entities;
        this.colorMeasurement = colorMeasurement;
        this.colorMeasurementDao = colorMeasurementDao;
        this.context = context;
    }

    @Override
    public MeasurementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeasurementAdapter.ViewHolder holder, int position) {
        Measurement entity = entities.get(position);
        holder.textView.setText(entity.Name);
        long millis = System.currentTimeMillis();
        if(colorMeasurement.R != -1)
        {
            colorMeasurement.Date = new java.util.Date(millis);
        }
        holder.relativeLayout.setOnClickListener(view ->
                {
                    if(entity.id == 0){
                        colorMeasurement.measurement_id = entities.size();
                    }
                    else{
                        colorMeasurement.measurement_id = entity.id;
                    }

                    //R == -1 means show results, so don't add this measurement do db
                    if(colorMeasurement.R != -1){
                        colorMeasurementDao.insert(colorMeasurement);
                    }
                    Intent intent = new Intent( context, ColorMeasurementActivity.class);
                    intent.putExtra("measurementId", entity.id);
                    context.startActivity(intent);
                });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
