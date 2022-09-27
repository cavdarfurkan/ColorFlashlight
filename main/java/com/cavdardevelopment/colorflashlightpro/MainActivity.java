package com.cavdardevelopment.colorflashlightpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class MainActivity extends AppCompatActivity {
    Intent intentFlash;
    Intent intentMors;

    private SeekBar seekBarBrightness;

    private Button buttonFlash, buttonSOS, buttonMors, buttonPreColors, buttonRGB;
    private ToggleButton buttonRainbow;

    private boolean isRainbow = false, isSOS = false;

    private int[] arrColor = {Color.WHITE, Color.RED, Color.rgb(255, 150, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(255, 0, 255), Color.rgb(0, 255, 255)};
    private int colorIndex = 0;

    private ColorPicker colorPicker;

    private int currentColor = Color.WHITE;

    private int showbigad = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        buttonFlash.setTextColor(arrColor[colorIndex]);

        SharedPreferences prefs = getSharedPreferences("showad", MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            int advalue = prefs.getInt("showad", 0);
            showbigad = advalue;
        }
    }

    private void init() {
        intentFlash = new Intent(this, FlashActivity.class);
        intentMors = new Intent(this, MorsActivity.class);

        seekBarBrightness = findViewById(R.id.seekBarBrightness);
        seekBarBrightness.setProgress(100);
        {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = 1F; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
            intentFlash.putExtra("MainActivity.brightness", 1F);
            intentMors.putExtra("MainActivity.brightness", 1F);
        }
        seekBarBrightness();

        buttonFlash = findViewById(R.id.buttonFlash);
        buttonSOS = findViewById(R.id.buttonSOS);
        buttonMors = findViewById(R.id.buttonMors);
        buttonPreColors = findViewById(R.id.buttonPreColors);
        buttonRGB = findViewById(R.id.buttonRGB);
        buttonRainbow = findViewById(R.id.buttonRainbow);

        colorPicker = new ColorPicker(MainActivity.this, 255, 0, 0);
    }

    private void seekBarBrightness(){
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float backLightValue = (float)progress/100;

                WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
                layoutParams.screenBrightness = backLightValue; // Set Value
                getWindow().setAttributes(layoutParams); // Set params

                intentFlash.putExtra("MainActivity.brightness", backLightValue);
                intentMors.putExtra("MainActivity.brightness", backLightValue);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // buttonFlash onClick
    public void startFlashActivity(View view){
        startActivity(intentFlash);
    }

    // buttonPreColors onClick
    public void changePreColor(View view){
        if(colorIndex == arrColor.length-1)
            colorIndex = 0;
        else
            colorIndex++;

        currentColor = arrColor[colorIndex];
        buttonFlash.setTextColor(arrColor[colorIndex]);
        intentFlash.putExtra("MainActivity.color", arrColor[colorIndex]);
    }

    // buttonRGB onClick
    public void rgbColor(View view){
        colorPicker.show();

        colorPicker.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(@ColorInt int color) {
                currentColor = colorPicker.getColor();
                buttonFlash.setTextColor(colorPicker.getColor());
                intentFlash.putExtra("MainActivity.color", colorPicker.getColor());
                colorPicker.dismiss();
            }
        });
    }

    // buttonRainbow onClick
    public void rainbow(View view){
        isRainbow = !isRainbow;
        intentFlash.putExtra("MainActivity.isRainbow", isRainbow);

        if(isRainbow)
            buttonFlash.setTextColor(Color.WHITE);
        else
            buttonFlash.setTextColor(currentColor);
    }

    // buttonMors onClick
    public void morsButton(View view){
        intentMors.setSelector(intentFlash);
        startActivity(intentMors);
    }

    // buttonSOS onClick
    public void sosButton(View view){
        isSOS = !isSOS;
        intentFlash.putExtra("MainActivity.SOS", isSOS);
    }
}