package com.example.android.hellosharedprefs;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button yes;

    Button test;

    // Constants for the notification actions buttons.
    private static final String ACTION_UPDATE_NOTIFICATION =
            "com.android.example.notifyme.ACTION_UPDATE_NOTIFICATION";

    private boolean willDrink = false;

    public static final int TEXT_REQUEST = 1;

    // Text view to display both count and color
    private TextView mShowCountTextView;
    
    // Key for current drink
    private final String DRINK_KEY = "drink";

    private final String DRINK = "Let's go!";

    private final String YOU_PASS = "Door unlocked!";

    private final String DEFAULT_MESSAGE = "Are you going to be drinking?";

    private final String CONTACT_OTHERS = "You need to contact friends or family";

    private final String TEST_INSTRUCTION = "You need to click pusheens 8 times to unlock";

    public static final String EXTRA_MESSAGE =
            "com.example.android.hellosharedprefs.extra.MESSAGE";

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;

    private NotificationManager mNotifyManager;

    // Shared preferences object
    private SharedPreferences mPreferences;

    // Name of shared preferences file
    private String sharedPrefFile =
            "com.example.android.hellosharedprefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the notification channel.
        createNotificationChannel();

        // Initialize views, color, preferences
        mShowCountTextView = findViewById(R.id.count_textview);

        yes = findViewById(R.id.yes_button);
        test = findViewById(R.id.reset_button);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        // Restore preferences
        willDrink = mPreferences.getBoolean(DRINK_KEY, false);
        mShowCountTextView.setText(String.format("%s", DEFAULT_MESSAGE));
        getLocation();
    }

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
/*                    Toast.makeText(this,
                            R.string.location_permission_denied,
                            Toast.LENGTH_SHORT).show();*/
                    Log.d(LOG_TAG, "Permission denied");
                }
                break;
        }
    }
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private void getLocation() {
        Log.d(LOG_TAG, "try to get location");
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d(LOG_TAG, "getLocation: permissions granted");
        }
    }

    /**
     * Unregisters the receiver when the app is being destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Creates a Notification channel, for OREO and higher.
     */
    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotifyManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            getString(R.string.notification_channel_name),
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    (getString(R.string.notification_channel_description));

            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    /**
     * Handles the onClick for the yes button.  Change the value of the
     * willDrink global to true and updates the TextView.
     *
     * @param view The view (Button) that was clicked.
     */
    public void planningDrink(View view) {
        willDrink = true;
        mShowCountTextView.setText(String.format("%s", DRINK));
        sendNotification();
        lock();
    }

    private static final String LOG_TAG =
            MainActivity.class.getSimpleName();


    public void launchTesting(View view) {
        Intent intent = new Intent(this, TestingActivity.class);
        intent.putExtra(EXTRA_MESSAGE, TEST_INSTRUCTION);
        startActivityForResult(intent, TEXT_REQUEST);
        Log.d(LOG_TAG, "Button clicked!");
    }
    /**
     * OnClick method for the "Notify Me!" button.
     * Creates and delivers a simple notification.
     */
    public void sendNotification() {

        // Sets up the pending intent to update the notification.
        // Corresponds to a press of the Update Me! button.
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,
                NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT);

        // Build the notification with all of the parameters using helper
        // method.
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();

        // Deliver the notification.
        mNotifyManager.notify(NOTIFICATION_ID, notifyBuilder.build());

    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                String reply =
                        data.getStringExtra(TestingActivity.EXTRA_REPLY);
                mShowCountTextView.setText(String.format("%s", reply));
                unlock();
                reset();
            }
        }
    }

    //TODO
    private void unlock() {
        yes.setVisibility(View.GONE);
        test.setVisibility(View.GONE);
    }
    //TODO
    private void lock() {

    }

    /**
     * Helper method that builds the notification.
     *
     * @return NotificationCompat.Builder: notification build with all the
     * parameters.
     */
    private NotificationCompat.Builder getNotificationBuilder() {

        // Set up the pending intent that is delivered when the notification
        // is clicked.
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity
                (this, NOTIFICATION_ID, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Build the notification with all of the parameters.
        NotificationCompat.Builder notifyBuilder = new NotificationCompat
                .Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_android)
                .setAutoCancel(true).setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return notifyBuilder;
    }

    /**
     * Handles the onClick for the Reset button.  Resets the global willDrink and
     * the defaults and resets the views to those
     * default values.
     */
    public void reset() {

        willDrink = false;

        // Clear preferences
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

    /**
     * Callback for activity pause.  Shared preferences are saved here.
     */
    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean(DRINK_KEY, willDrink);
        preferencesEditor.apply();
    }


}
