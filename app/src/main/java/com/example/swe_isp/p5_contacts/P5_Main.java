package com.example.swe_isp.p5_contacts;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.swe_isp.R;

import android.widget.ListView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager;
import android.content.pm.ActivityInfo;
import android.util.Log;
import java.util.List;

// telephony

import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

// email

import android.net.NetworkInfo;
import android.app.AlertDialog;
import android.net.ConnectivityManager;

// web

import android.webkit.WebView;
import android.webkit.WebViewClient;

// maps

public class P5_Main extends AppCompatActivity {
    String[] items = new String[] { "Call", "Write", "Visit", "Find" };

    private static final int PERMISSION_REQUEST_CODE = 1; // Permission request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p5_main);

        ListView contactListView = (ListView) findViewById(R.id.contactListView);
        contactListView.setAdapter(new ContactAdapter(this, items));
        contactListView.setTextFilterEnabled(true);
        contactListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);

                if(selectedValue.equals("Call")) {
                    // Request permission if not granted
                    if (ContextCompat.checkSelfPermission(P5_Main.this, Manifest.permission.READ_PHONE_STATE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(P5_Main.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                PERMISSION_REQUEST_CODE);
                    } else {
                        call("tel:7097587091");
                    }
                }
                else if(selectedValue.equals("Write")) {
                    sendEmail();
                }
                else if(selectedValue.equals("Visit")) {
                    loadWebsite("https://www.cna.nl.ca");
                }
                else if(selectedValue.equals("Find")) {
                    loadWebsite("https://www.google.com/maps?q=College of the North Atlantic Engineering Technology");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the action
                call("tel:7097587091");
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(P5_Main.this, "Permission denied! Cannot access phone state.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void call(String pn) {
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        // Check if permission is granted before listening
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            telephonyManager.listen(phoneListener,
                    PhoneStateListener.LISTEN_CALL_STATE);
        }

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse(pn));
        try {
            startActivity(Intent.createChooser(callIntent, "Complete Action Using"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(P5_Main.this, "There are no phone clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended, need detect flag
                // from CALL_STATE_OFFHOOK

                if (isPhoneCalling) {
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    isPhoneCalling = false;
                    finish();
                }
            }
        }
    }

    private void sendEmail() {
        if(!isNetworkAvailable())
            new AlertDialog.Builder(P5_Main.this).
                    setTitle("Error").setMessage("No Network Connection").
                    setNeutralButton("Close", null).show();
        else {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"bc170264@gmail.com"});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ETC Programs Info");
            // i.putExtra(Intent.EXTRA_TEXT, "body of email");
            try {
                startActivity(Intent.createChooser(emailIntent, "Complete Action Using"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(P5_Main.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }

    public void loadWebsite(String s) {

        Intent webIntent = new Intent(getApplicationContext(), WebIntent.class);
        webIntent.putExtra("url", s);
        startActivity(webIntent);

    }

    private void openLocation() {
        if (!isNetworkAvailable()) {
            new AlertDialog.Builder(P5_Main.this)
                    .setTitle("Error")
                    .setMessage("No Network Connection")
                    .setNeutralButton("Close", null)
                    .show();
        } else {
            // Create a WebView instance
            WebView webView = new WebView(this);

            // Set up WebView settings
            webView.getSettings().setJavaScriptEnabled(true); // Enable JavaScript
            webView.getSettings().setAllowFileAccess(false); // Disable file access if not needed
            webView.getSettings().setAllowContentAccess(false); // Disable content access if not needed
            webView.getSettings().setMediaPlaybackRequiresUserGesture(false); // Disable media playback access
            webView.setWebViewClient(new WebViewClient()); // Links open in WebView

            // Load the Google Maps URL
            String locationUrl = "https://www.google.com/maps?q=153+Ridge+Road,+St.+John's,+NL";
            webView.loadUrl(locationUrl);

            // Add the WebView to your activity's layout
            setContentView(webView);  // This will replace the current layout with the WebView
        }
    }
}