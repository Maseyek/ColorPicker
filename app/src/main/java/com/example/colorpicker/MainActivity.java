package com.example.colorpicker;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.IntSummaryStatistics;

public class MainActivity extends AppCompatActivity {


    public Button Upload_image, Take_Photo;
    public ImageButton Confirm, Save;
    public TextView rgbValue;
    public ImageView uploadedImage, colorDisplay;
    public String rgbColor;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    public int precision = 5, evX, evY;
    Bitmap bitmap_Temp, bitmap_Calc;
    boolean isFresh;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Upload_image = findViewById(R.id.Upload_image);
        Take_Photo = findViewById(R.id.Take_photo);
        rgbValue = findViewById(R.id.rgbcolor);
        colorDisplay = findViewById(R.id.color_display);
        uploadedImage = findViewById(R.id.uploaded_image);
        Confirm =findViewById(R.id.confirm_button);
        Save = findViewById(R.id.save_button);


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

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this, "it works", Toast.LENGTH_LONG).show();
                int[] r = new int[precision*precision];
                int[] g = new int[precision*precision];
                int[] b = new int[precision*precision];



                int count = 0;
                for (int i = -(precision/2); i < (precision/2); i++)
                {
                    for(int j = -(precision/2); j < (precision/2); j++)
                    {
                        int touchColor = getColor(bitmap_Calc, evX + i, evY + j);

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
                rgbColor = String.valueOf(averageR) + "," + String.valueOf(averageG) + "," + String.valueOf(averageB);
                rgbValue.setText("RGB:    " + rgbColor);

            }
        });



        uploadedImage.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                try {
                    final int action = motionEvent.getAction();
                    evX = (int) motionEvent.getX();
                    evY = (int) motionEvent.getY();
                    if (action==MotionEvent.ACTION_DOWN)
                    {
                        if(isFresh == TRUE) {
                            uploadedImage.setDrawingCacheEnabled(true);
                            bitmap_Temp = Bitmap.createBitmap(uploadedImage.getDrawingCache());
                            bitmap_Calc = Bitmap.createBitmap(uploadedImage.getDrawingCache());
                            uploadedImage.setDrawingCacheEnabled(false);
                            isFresh = FALSE;
                        }
                        bitmap_Temp = bitmap_Calc.copy(bitmap_Calc.getConfig(), true);
                        uploadedImage.setImageBitmap(bitmap_Calc);

                    }





                    int touchColor = getColor(bitmap_Calc, evX, evY);

                    int r = (touchColor >> 16) & 0xFF;
                    int g = (touchColor >> 8) & 0xFF;
                    int b = (touchColor >> 0) & 0xFF;
                    rgbColor = String.valueOf(r) + "," + String.valueOf(g) + "," + String.valueOf(b);
                    rgbValue.setText("RGB:    " + rgbColor);

                    if (action==MotionEvent.ACTION_UP)
                    {
                        colorDisplay.setBackgroundColor(getColor(bitmap_Calc, evX , evY));
                        for (int i = -(precision/2); i < (precision/2); i++) {
                            for (int j = -(precision/2); j < (precision/2); j++) {
                                bitmap_Temp.setPixel(evX + i, evY + j, Color.BLACK);
                            }
                        }

                        uploadedImage.setImageBitmap(bitmap_Temp);

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

//    private int getColor(ImageView uploadedImage, int evX, int evY){
//        uploadedImage.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(uploadedImage.getDrawingCache());
//        uploadedImage.setDrawingCacheEnabled(false);
//        return bitmap.getPixel(evX, evY);
//
//    }

    private int getColor(Bitmap bitmap, int evX, int evY){

        return bitmap.getPixel(evX, evY);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && !data.equals(null)){
            Uri Image = data.getData();
            uploadedImage.setImageURI(Image);
            isFresh = TRUE;
        }
        else if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap)data.getExtras().get("data");
            uploadedImage.setImageBitmap(photo);
            isFresh = TRUE;
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