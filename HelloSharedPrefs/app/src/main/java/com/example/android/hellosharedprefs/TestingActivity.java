package com.example.android.hellosharedprefs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TestingActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE =
            "com.example.android.hellosharedprefs.extra.MESSAGE";


    public static final int TEXT_REQUEST = 1;

    private int current_score;

    private int try_count;

    private int retry_count;

    Double LONG;

    Double LAT;

    private final String CONTACT_OTHERS = "You need to contact friends or family. ";

    private final String RETRY_MESSAGE = " Or you can retry.";

    private final String NO_RETRY = " You use up your retry attempts.";

    private final String TEXT_MESSAGE = "Pick me up, since I am drunk.";

    public static Activity fa;
    public static boolean isThreadEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fa = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = findViewById(R.id.text_message);
        textView.setText(message);
        current_score = 0;
        try_count = 0;
        retry_count = 0;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Button clickMe = findViewById(R.id.button_testing);
        clickMe.setVisibility(View.GONE);
    }

    private final int PASS_COUNT = 5;

    private final int TOTAL_COUNT = 8;

    private final int MAX_RETRY = 3;

    private final String YOU_PASS = "Door unlocked!";

    private boolean canTry = true;
    private boolean isRetrying = false;

    public static final String EXTRA_REPLY =
            "com.example.android.hellosharedprefs.extra.REPLY";


    public void clickCount(View view) {


        Button clickMe = findViewById(R.id.button_testing);
        Button start = findViewById(R.id.button_testing);
        TextView textView = findViewById(R.id.text_message);
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        clickMe.setBackgroundColor(color);
        if (!isRetrying) {
            if (canTry) {
                current_score++;
                if (current_score >= PASS_COUNT) {
                    current_score = 0;
                    try_count = 0;


                    Intent intent = new Intent(this, ColorActivity.class);
                    isThreadEnd = true;
                    startActivity(intent);
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_REPLY, YOU_PASS);
                    setResult(RESULT_OK, replyIntent);
                    finish();

                } else if (retry_count > MAX_RETRY) {
                    textView.setText(CONTACT_OTHERS + NO_RETRY);
                    clickMe.setText("No more retry");
                    canTry = false;
//                  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    sendMessage();
                    //phoneCall();
                } //TODO: BUG
            }
        } else {
            isRetrying = false;
            textView.setText(CONTACT_OTHERS);
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LONG = location.getLongitude();
            LAT = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

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
    public static Point getDisplaySize(@NonNull Context context) {
        Point point = new Point();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getSize(point);
        return point;
    }

    private void setButtonRandomPosition(final Button button){
        int x = getDisplaySize(this).x;
        int y = getDisplaySize(this).y;
        int randomX = new Random().nextInt(x/2) + x/4;
        int randomY = new Random().nextInt(y/2) + y/4;
        if(!isThreadEnd) {
            try {
                button.setX(randomX);
                button.setY(randomY);
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }

        }
    }

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();

    private void startRandomButton(final Button b) {
        if(canTry) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    setButtonRandomPosition(b);
                    Log.d(LOG_TAG, Integer.toString(try_count));
                    try_count++;
                }
            }, 1, 1000);//Update button every second

            new CountDownTimer(TOTAL_COUNT * 1000, 1000) {
                TextView text = findViewById(R.id.text_message);
                public void onTick(long millisUntilFinished) {
                    text.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    text.setText("Over!");
                    current_score = 0;
                    try_count = 0;
                    text.setText(CONTACT_OTHERS + RETRY_MESSAGE);
                    Button clickMe = findViewById(R.id.button_testing);
                    clickMe.setVisibility(View.GONE);
                    retry_count++;
                    isRetrying = true;
                    Button start = findViewById(R.id.button_start);
                    start.setText("Retry");
                    start.setVisibility(View.VISIBLE);
                    if (retry_count > MAX_RETRY) {
                        text.setText(CONTACT_OTHERS + NO_RETRY);
                        start.setText("No more retry");
                        canTry = false;
                    }
                }
            }.start();
        }
    }

    FusedLocationProviderClient mFusedLocationClient;
    public void StartTesting(View view) {
        if(canTry) {
            try_count = 0;
            TextView text = findViewById(R.id.text_message);
            Button clickMe = findViewById(R.id.button_testing);
            Button start = findViewById(R.id.button_start);
            start.setVisibility(View.GONE);
            clickMe.setVisibility(View.VISIBLE);
            startRandomButton(clickMe);
            retry_count++;
        } else {

//           mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            sendMessage();
            //phoneCall();
        }
    }

    private void phoneCall() {
        String phone = "6472705503";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    private String locationMessage = "";
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Location mLastLocation = location;
                                LAT = mLastLocation.getLatitude();
                                LONG = mLastLocation.getLongitude();
                                locationMessage =
                                        getString(R.string.location_text,
                                                mLastLocation.getLatitude(),
                                                mLastLocation.getLongitude(),
                                                mLastLocation.getTime());
                            } else {
                                locationMessage = "No location";
                            }
                        }
                    });
        }
    }

    private final int REQUEST_LOCATION_PERMISSION = 10;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                // If the permission is granted, get the location,
                // otherwise, show a Toast
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    Toast.makeText(this,
                            R.string.notification_text,
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


//    public void testtwotransition{
//
//
//
//
//
//
//
//    }

}
