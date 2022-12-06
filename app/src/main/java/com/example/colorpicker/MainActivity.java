package com.example.colorpicker;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class MainActivity extends AppCompatActivity {


    public Button Upload_image, Take_Photo;
    public TextView rgbValue;
    public ImageView uploadedImage, colorDisplay;
    public String rgbColor;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public int precision = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Upload_image = findViewById(R.id.Upload_image);
        Take_Photo = findViewById(R.id.Take_photo);
        rgbValue = findViewById(R.id.rgbcolor);
        colorDisplay = findViewById(R.id.color_display);
        uploadedImage = findViewById(R.id.uploaded_image);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


                    int[] r = new int[precision*precision];
                    int[] g = new int[precision*precision];
                    int[] b = new int[precision*precision];



                    int count = 0;
                    for (int i = -8; i < 8; i++)
                    {
                        for(int j = -8; j < 8; j++)
                        {
                            int touchColor = getColor(uploadedImage, evX + i, evY + j);

                            r[count] = (touchColor >> 16) & 0xFF;
                            g[count] = (touchColor >> 8) & 0xFF;
                            b[count] = (touchColor >> 0) & 0xFF;
                            count++;

                        }
                    }

                    IntSummaryStatistics statR = Arrays.stream(r).summaryStatistics();
                    IntSummaryStatistics statG = Arrays.stream(g).summaryStatistics();
                    IntSummaryStatistics statB = Arrays.stream(b).summaryStatistics();

                    int minR = statR.getMin();
                    int maxR = statR.getMax();
                    int averageR = (int) statR.getAverage();

                    int minG = statG.getMin();
                    int maxG = statG.getMax();
                    int averageG = (int) statG.getAverage();

                    int minB = statB.getMin();
                    int maxB = statB.getMax();
                    int averageB = (int) statB.getAverage();


                    /*int touchColor = getColor(uploadedImage, evX, evY);

                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;*/
                    rgbColor = String.valueOf(averageR) + "," + String.valueOf(averageG) + "," + String.valueOf(averageB);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                openSettingsForResult();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openSettingsForResult() {
        Intent intent = new Intent(this, SettingsActivity.class);
        //https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
        intent.putExtra("precision", precision);
        settingsActivityResultLauncher.launch(intent);
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> settingsActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    assert data != null;
                    setPrecision(Integer.parseInt(String.valueOf(data.getData())));
                }
            });

    void setPrecision(int p){
        if(p > 0)
            precision = p;
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