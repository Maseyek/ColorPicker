package com.example.colorpicker.Database;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpicker.R;

import java.util.List;

public class ColorMeasurementAdapter extends RecyclerView.Adapter<ColorMeasurementAdapter.ViewHolder> {
    private List<ColorMeasurement> entities;

    public ColorMeasurementAdapter(List<ColorMeasurement> entities) {
        this.entities = entities;
    }

    @Override
    public ColorMeasurementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ColorMeasurementAdapter.ViewHolder holder, int position) {
        ColorMeasurement entity = entities.get(position);
        holder.textView.setText(entity.Date.toString());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: ",Toast.LENGTH_LONG).show();
            }
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
