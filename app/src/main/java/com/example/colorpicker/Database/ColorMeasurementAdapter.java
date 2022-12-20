package com.example.colorpicker.Database;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.colorpicker.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ColorMeasurementAdapter extends RecyclerView.Adapter<ColorMeasurementAdapter.ViewHolder> {
    private List<ColorMeasurement> entities;
    private PopupWindow popupWindow;
    private View parent;
    private View popupView;
    public ColorMeasurementAdapter(List<ColorMeasurement> entities, PopupWindow popupWindow, View parent) {
        this.entities = entities;
        this.popupWindow = popupWindow;
        this.parent = parent;
        // Get the content view of the pop-up window
        popupView = popupWindow.getContentView();
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
        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(entity.Date != null) {

            // Format the Date object and print the result
            String formattedDate = sdf.format(entity.Date);
            holder.textView.setText(formattedDate);
        }else if (entity.Date == null)
        {
            Date date = new Date();
            holder.textView.setText(sdf.format(date));
        }


        holder.imageView.setBackgroundColor(android.graphics.Color.rgb(entity.R, entity.G, entity.B));
        holder.relativeLayout.setOnClickListener(view ->
                {
                    // Find the text view and view by their ID
                    TextView textView = popupView.findViewById(R.id.textView);
                    ImageView imageView = popupView.findViewById(R.id.imageView);

                    String text = "RGB: " + entity.R+ ", " + entity.G+ ", " + entity.B + "\n" +
                            "RGBMax " + entity.RMax+ ", " + entity.GMax+ ", " + entity.BMax + "\n" +
                            "RGBMin " + entity.RMin+ ", " + entity.GMin+ ", " + entity.BMin + "\n" +
                            "Median " + entity.RMedian+ ", " + entity.GMedian + ", "+ entity.BMedian + "\n";
                            // Modify the text view and view as needed
                    textView.setText(text); // green color
                    imageView.setBackgroundColor(android.graphics.Color.rgb(entity.R, entity.G, entity.B));

                    // Show the pop-up window at the center location of root relative layout
                    popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
                });
    }

    @Override
    public int getItemCount() {
        return entities.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            imageView = (ImageView) view.findViewById(R.id.imageView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
