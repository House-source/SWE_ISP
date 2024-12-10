package com.example.swe_isp.p1_programs;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ListView;
import android.content.Intent;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.AdapterView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.example.swe_isp.R;

public class P1_Main extends AppCompatActivity {

    static final String[] PROGRAMS = new String[] { "Architectural",
            "Civil", "Geomatics", "Computing Systems", "Biomedical",
            "Instrumentation", "Electrical (Power)",
            "Telecommunications", "Chemical Processing", "Industrial", "Mechanical",
            "Manufacturing", "Petroleum" };

    private String loc = new String();
    private String title = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_main);

        ListView programListView = (ListView) findViewById(R.id.programListView);
        programListView.setAdapter(new ProgramAdapter(this, PROGRAMS));
        programListView.setTextFilterEnabled(true);

        programListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedValue = (String) parent.getItemAtPosition(position);

                StringBuffer sb = new StringBuffer();
                sb.append("http://branko-cirovic.appspot.com/etcapp/programs/");

                title = selectedValue;

                if(selectedValue.equals("Architectural"))
                    sb.append("ae.xml");
                else if(selectedValue.equals("Biomedical"))
                    sb.append("be.xml");
                else if(selectedValue.equals("Chemical Processing"))
                    sb.append("cp.xml");
                else if(selectedValue.equals("Civil"))
                    sb.append("ce.xml");
                else if(selectedValue.equals("Computing Systems"))
                    sb.append("cs.xml");
                else if(selectedValue.equals("Electrical (Power and Controls)"))
                    sb.append("ee.xml");
                else if(selectedValue.equals("Geomatics"))
                    sb.append("gs.xml");
                else if(selectedValue.equals("Instrumentation"))
                    sb.append("ic.xml");
                else if(selectedValue.equals("Management Systems"))
                    sb.append("ms.xml");
                else if(selectedValue.equals("Mechanical"))
                    sb.append("mee.xml");
                else if(selectedValue.equals("Manufacturing"))
                    sb.append("mae.xml");
                else if(selectedValue.equals("Petroleum"))
                    sb.append("pe.xml");

                loc = sb.toString();

                System.out.println(loc);

                if(!isNetworkAvailable())
                    new AlertDialog.Builder(P1_Main.this).
                            setTitle("Error").setMessage("No Network Connection").
                            setNeutralButton("Close", null).show();
                else {
                    new GetXML().execute("");
                }

            }
        });
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

    private class GetXML extends AsyncTask<String, Void, String> {
        String src = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(loc);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                src = readStream(con.getInputStream());

            } catch (Exception e) {
                e.printStackTrace();
            }

            return src;
        }

        @Override
        protected void onPostExecute(String result) {
            if(src == null)
                new AlertDialog.Builder(P1_Main.this).
                        setTitle("Error").setMessage("No Courses Found").
                        setNeutralButton("Close", null).show();
            else {
                Intent coursesScreen = new Intent(getApplicationContext(), CoursesActivity.class);
                coursesScreen.putExtra("source", src);
                coursesScreen.putExtra("programTitle", title);
                startActivity(coursesScreen);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        String line = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }
}
