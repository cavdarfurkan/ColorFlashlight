package com.cavdardevelopment.colorflashlightpro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class FlashActivity extends AppCompatActivity {
    private int[] arrColor = {Color.WHITE, Color.RED, Color.rgb(255, 150, 0), Color.YELLOW, Color.GREEN, Color.BLUE, Color.rgb(255, 0, 255), Color.rgb(0, 255, 255)};

    private Thread timer1, timer2, timer3;
    public boolean isRunning = false;

    // 0 -> dot 1 -> line
    private int[] morseSOS = {0, 0, 0, 1, 1, 1, 0, 0, 0};
    private ArrayList<Integer> morsSeqList;
    private boolean isMors = false;

    private ConstraintLayout constraintLayout;
    private int color;
    private boolean isRainbow = false;
    private boolean isSOS = false;
    private Button buttonExit;

    private float brightness = 1F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);

        Intent intent = getIntent();
        color = intent.getIntExtra("MainActivity.color", -1);
        isRainbow = intent.getBooleanExtra("MainActivity.isRainbow", isRainbow);
        brightness = intent.getFloatExtra("MainActivity.brightness", brightness);
        isSOS = intent.getBooleanExtra("MainActivity.SOS", isSOS);
        isMors = intent.getBooleanExtra("MorsActivity.isMors", isMors);

        init();

        { // Set Brightness
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes(); // Get Params
            layoutParams.screenBrightness = brightness; // Set Value
            getWindow().setAttributes(layoutParams); // Set params
        }

        changeBackground(color);

        if(isSOS){
            isMors = true;
            for(Integer e : morseSOS){
                morsSeqList.add(e);
            }
        }

        if(isMors){
            if(!isSOS)
                morsSeqList = intent.getIntegerArrayListExtra("MorsActivity.morsCode");
            if(isRainbow){
                morsCodeRainbow();
            }
            else {
                morsCode();
            }
        }
        else if (isRainbow){
            rainbowFlash();
        }
        else{
            changeBackground(color);
        }
    }

    private void init(){
        morsSeqList = new ArrayList<>();

        constraintLayout = findViewById(R.id.backgroundLayout);
        buttonExit = findViewById(R.id.buttonExit);
        buttonExit.animate().alpha(0.0f).setDuration(3000).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        buttonExit.setAlpha(1.0f);
        buttonExit.animate().alpha(0f).setDuration(3000).start();
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }

    private void morsCode(){
        isRunning = true;
        timer2 = new Thread(){
            int i = 0;

            @Override
            public void run() {
                try {
                    while (isRunning) {
                        if(i == morsSeqList.size()){
                            sleep(1000);
                            i = 0;
                        }

                        changeBackground(color);
                        if (morsSeqList.get(i) == 0) {
                            sleep(200);
                        } else if (morsSeqList.get(i) == 1) {
                            sleep(700);
                        }

                        changeBackground(Color.BLACK);
                        sleep(500);
                        i++;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        timer2.start();
    }

    private void morsCodeRainbow(){
        isRunning = true;
        timer3 = new Thread(){
            int i = 0;
            int colorI = 0;

            @Override
            public void run() {
                try {
                    while (isRunning) {
                        if(i == morsSeqList.size()){
                            sleep(1000);
                            i = 0;
                        }
                        if(colorI == arrColor.length-1)
                            colorI = 0;

                        changeBackground(arrColor[colorI]);
                        if (morsSeqList.get(i) == 0) {
                            sleep(200);
                        } else if (morsSeqList.get(i) == 1) {
                            sleep(700);
                        }

                        changeBackground(Color.BLACK);
                        sleep(500);
                        i++;
                        colorI++;
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        timer3.start();
    }

    private void rainbowFlash(){
        isRunning = true;
        timer1 = new Thread() {
            boolean increase = true;
            int r = 0, g = 0, b = 0;

            @Override
            public void run() {
                try {
                    while (isRunning) {
                        if (increase) {
                            if (r <= 254)
                                r++;
                            else if (g <= 254)
                                g++;
                            else if (b <= 254)
                                b++;
                            else
                                increase = false;
                        } else {
                            if (r > 0)
                                r--;
                            else if (g > 0)
                                g--;
                            else if (b > 0)
                                b--;
                            else
                                increase = true;
                        }

                        sleep(15);
                        changeBackground(r, g, b);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer1.start();
    }

    // rainbow effect
    private void changeBackground(final int r, final int g, final int b){
        int c = Color.rgb(r, g, b);
        changeBackground(c);
    }

    private void changeBackground(final int color){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                constraintLayout.setBackgroundColor(color);
            }
        });
    }

    // buttonExit onClick
    public void buttonExitOnClick(View view){
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();

        try{
            isRunning = false;

            timer1.interrupt();
            timer2.interrupt();
            timer3.interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}