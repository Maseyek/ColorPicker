package com.example.colorpicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    public Button Upload_image, Take_Photo;
    public TextView rgbValue;
    public ImageView uploadedImage, colorDisplay;
    public String rgbColor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Upload_image = findViewById(R.id.Upload_image);
        Take_Photo = findViewById(R.id.Take_photo);
        rgbValue = findViewById(R.id.rgbcolor);
        colorDisplay = findViewById(R.id.color_display);
        uploadedImage = findViewById(R.id.uploaded_image);

        Upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickImage, 1);
            }
        });

        uploadedImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    final int action = motionEvent.getAction();
                    final int evX = (int) motionEvent.getX();
                    final int evY = (int) motionEvent.getY();
                    int touchColor = getColor(uploadedImage, evX, evY);

                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;
                    rgbColor = String.valueOf(r) + "," + String.valueOf(g) + "," + String.valueOf(b);
                    rgbValue.setText("RGB:    " + rgbColor);

                    if (action==MotionEvent.ACTION_UP)
                    {
                        colorDisplay.setBackgroundColor(touchColor);
                    }
                }catch (Exception e){}


                return true;
            }
        });

    }

    private int getColor(ImageView uploadedImage, int evX, int evY){
        uploadedImage.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(uploadedImage.getDrawingCache());
        uploadedImage.setDrawingCacheEnabled(false);
        return bitmap.getPixel(evX, evY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && !data.equals(null)){
            Uri Image = data.getData();
            uploadedImage.setImageURI(Image);

        }
    }
}