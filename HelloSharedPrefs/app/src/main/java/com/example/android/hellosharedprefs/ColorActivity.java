package com.example.android.hellosharedprefs;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.android.hellosharedprefs.TestingActivity.EXTRA_REPLY;

public class ColorActivity extends AppCompatActivity {

    TextView timerTextView;
    long startTime = 0;
    long millis;
    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            millis = System.currentTimeMillis() - startTime;

            timerTextView.setText(millis + "ms");

            timerHandler.postDelayed(this, 100);
        }
    };

    private final String YOU_PASS = "Door unlocked!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TestingActivity.fa.finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        
        timerTextView = (TextView) findViewById(R.id.text_clock);

        Button b = (Button) findViewById(R.id.button_color);
        b.setText("start");
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {

                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                    if(millis < 500) {
                        Intent replyIntent = new Intent();
                        replyIntent.putExtra(EXTRA_REPLY, YOU_PASS);
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    } else {
                        phoneCall();
                    }
                } else {


                    b.setText("stop");
                    try {
                        Random rnd = new Random();
                        Thread.sleep((rnd.nextInt(200) + 200 )* 10);
                        Button colorButton = findViewById(R.id.button_color);
                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                        colorButton.setBackgroundColor(color);
                        millis = 0;
                        startTime = System.currentTimeMillis();
                        timerTextView.setText(millis + "ms");
                        timerHandler.postDelayed(timerRunnable, 0);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void phoneCall() {
        String phone = "6472705503";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Button b = (Button)findViewById(R.id.button_color);
        b.setText("start");
    }
}
