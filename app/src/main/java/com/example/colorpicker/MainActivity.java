package com.example.colorpicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    public Button Upload_image, Take_Photo;
    public TextView rgbValue;
    public ImageView uploadedImage, colorDisplay;
    public String rgbColor;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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
        Take_Photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });




        uploadedImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    final int action = motionEvent.getAction();
                    final int evX = (int) motionEvent.getX();
                    final int evY = (int) motionEvent.getY();
                    int precision = 50;
                    /*int touchColor = 0;*/
                    int r = 0;
                    int g = 0;
                    int b = 0;
                    int count = 0;
                    for (int i = -6; i < 6; i++)
                    {
                        for(int j = -6; j < 6; j++)
                        {
                            int touchColor = getColor(uploadedImage, evX + i, evY + j);

                            r += (touchColor >> 16) & 0xFF;
                            g += (touchColor >> 8) & 0xFF;
                            b += (touchColor >> 0) & 0xFF;
                            count++;
                            if (count > 1)
                            {
                                r = r/2;
                                g = g/2;
                                b = b/2;
                            }
                        }
                    }



                    /*int touchColor = getColor(uploadedImage, evX, evY);

                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;*/
                    rgbColor = String.valueOf(r) + "," + String.valueOf(g) + "," + String.valueOf(b);
                    rgbValue.setText("RGB:    " + rgbColor);

                    if (action==MotionEvent.ACTION_UP)
                    {
                        colorDisplay.setBackgroundColor(getColor(uploadedImage, evX , evY));
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
        else if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            uploadedImage.setImageBitmap(photo);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

}