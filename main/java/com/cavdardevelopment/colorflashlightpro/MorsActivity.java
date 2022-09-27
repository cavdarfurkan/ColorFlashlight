package com.cavdardevelopment.colorflashlightpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MorsActivity extends AppCompatActivity {

    Intent flashIntent;

    ArrayList<Integer> morsSeqList;

    private Button buttonDot, buttonLine, buttonClear, buttonFlash2;
    private TextView textViewMors;

    private float brightness;

    private boolean isSOS = false;

    private int showbigad = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mors);

        Intent intent = getIntent();
        flashIntent = intent.getSelector();
        brightness = intent.getFloatExtra("MainActivity.brightness", brightness);

        isSOS = flashIntent.getBooleanExtra("MainActivity.SOS", isSOS);

        init();

        { // Set Brightness
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = brightness; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
        }

        SharedPreferences prefs = getSharedPreferences("showad", MODE_PRIVATE);
        String restoredText = prefs.getString("text", null);
        if (restoredText != null) {
            int advalue = prefs.getInt("showad", 0);
            showbigad = advalue;
        }
    }

    private void init() {
        morsSeqList = new ArrayList<>();

        buttonDot = findViewById(R.id.buttonDot);
        buttonLine = findViewById(R.id.buttonLine);
        buttonClear = findViewById(R.id.buttonClear);
        buttonFlash2 = findViewById(R.id.buttonFlash2);

        textViewMors = findViewById(R.id.textViewMors);

        if(!isSOS)
            textViewMors.setText("");
        else{
            textViewMors.setText(R.string.sos_seq);
        }
    }

    // buttonDot onClick
    public void buttonDot(View view){
        if(!isSOS){
            morsSeqList.add(0);
            textViewMors.append(".");
        }
    }

    // buttonLine onClick
    public void buttonLine(View view){
        if(!isSOS) {
            morsSeqList.add(1);
            textViewMors.append("_");
        }
    }

    // buttonClear onClick
    public void buttonClear(View view){
        if(!isSOS) {
            morsSeqList.clear();
            textViewMors.setText("");
        }
    }

    // buttonFlash2 onClick
    public void buttonFlash2(View view){
        flashIntent.putExtra("MorsActivity.isMors", true);
        flashIntent.putExtra("MorsActivity.morsCode", morsSeqList);
        startActivity(flashIntent);
    }

    // buttonBack onClick
    public void buttonBackOnClick(View view){
        finish();
    }
}