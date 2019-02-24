package com.example.android.hellosharedprefs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
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

    private final String TEST_INSTRUCTION2 = "Test your reflexes, get ready!";

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
        TestingActivity.fa.finish();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);
        TextView instruction = findViewById(R.id.text_message2);
        instruction.setText(TEST_INSTRUCTION2);
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
                        //phoneCall();
                        sendMessage();
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

    private void sendMessage() {
//        getLocation();
//        Intent sendIntent = new Intent();
//        String phone = "6472705503";
//        String sms = "Hi, this is Kotlin. I'm drunk and cannot drive myself. Please pick me up! I'm at ";
//        locationMessage = sms + "https://www.google.com/maps/search/?api=1&" + "query=" + LAT + "," + LONG;
//        sendIntent.setAction(Intent.ACTION_SENDTO);
//        sendIntent.setData(Uri.parse(phone));
//        sendIntent.putExtra(Intent.EXTRA_TEXT, locationMessage);
//        sendIntent.setType("text/plain");
//        startActivity(sendIntent);

//        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        //lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        //      if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
        //        return;
        //  }
        //Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //LAT = location.getLongitude();
        //LONG = location.getLatitude();

        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        double LAT = 0;
        double LONG = 0;
        if (location != null) {
            LAT=location.getLatitude();
            LONG=location.getLongitude();
            Log.d("old","lat :  "+LAT);
            Log.d("old","long :  "+LONG);
            //this.onLocationChanged(location);
        }

        String phone = "6472705503";
        String sms = "Hi, this is Kotlin. I'm drunk and cannot drive myself. Please pick me up! I'm at ";
        sms = sms + "https://www.google.com/maps/search/?api=1&" + "query=" + LAT + "," + LONG;
        //"https://www.google.com/maps/place/43%C2%B046'56.4%22N+79%C2%B011'19.6%22W/@43.7823333,-79.1909665,17z/data=!3m1!4b1!4m6!3m5!1s0x0:0x0!7e2!8m2!3d43.7823299!4d-79.1887872";
/*        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(phone));
        intent.putExtra("sms_body", sms);
     startActivity(intent);*/

        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("sms:" + phone));
        intent.putExtra("sms_body", sms);
        startActivity(intent);
    }
}
