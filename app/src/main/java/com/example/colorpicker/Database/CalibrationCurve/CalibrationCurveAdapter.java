package com.example.colorpicker.Database.CalibrationCurve;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.colorpicker.CalibrationCurveActivity;
import com.example.colorpicker.Database.AppDatabase;
import com.example.colorpicker.R;

import java.util.List;

public class CalibrationCurveAdapter extends RecyclerView.Adapter<CalibrationCurveAdapter.ViewHolder> {
    private List<CalibrationCurve> entities;
    private CalibrationCurveDao calibrationCurveDao;
    private Context context;

    public CalibrationCurveAdapter(List<CalibrationCurve> entities, CalibrationCurveDao calibrationCurveDao, Context context) {
        this.entities = entities;
        this.calibrationCurveDao = calibrationCurveDao;
        this.context = context;
    }

    @Override
    public CalibrationCurveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalibrationCurveAdapter.ViewHolder holder, int position) {
        CalibrationCurve entity = entities.get(position);
        holder.textView.setText(entity.Name);
        holder.relativeLayout.setOnClickListener(view ->
                {
                    Intent intent = new Intent( context, CalibrationCurveActivity.class);
                    intent.putExtra("calibrationCurveId", entity.id);
                    context.startActivity(intent);
                });
        holder.relativeLayout.setOnLongClickListener(view ->
        {
            AppDatabase db = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").allowMainThreadQueries().build();
            CalibrationCurveDao calibrationCurveDao = db.calibrationCurveDao();
            calibrationCurveDao.delete(entity);
            super.notifyItemRemoved(entities.indexOf(entity));
            entities.remove(entity);
            return true;
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
