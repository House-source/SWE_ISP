package com.example.swe_isp.p3_calendar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import android.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.ListView;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.AdapterView;
import android.content.Intent;

import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.view.ViewGroup;
import android.content.DialogInterface;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import com.example.swe_isp.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class P3_Main extends AppCompatActivity implements OnDateSelectedListener {

    private DateFormat FORMATTER = SimpleDateFormat.getDateInstance();
    private OneDayDecorator oneDayDecorator;

    @BindView(R2.id.calendarView)
    MaterialCalendarView widget;

    ArrayList<CalendarDay> dates;
    EventDecorator events;
    static ArrayList<Event> xmlEvents;
    static ArrayList<Event> selectedEvents;

    CalendarDay selectedDate;
    String filename = new String("cal_data.xml");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p3_main);

        oneDayDecorator = new OneDayDecorator(P3_Main.this);
        ButterKnife.bind(this);

        dates = new ArrayList<>();
        xmlEvents = new ArrayList<Event>();
        selectedEvents = new ArrayList<>();

        if(!isNetworkAvailable()) {
            new AlertDialog.Builder(this).
                    setTitle("Error").setMessage("No Network Connection").
                    setNeutralButton("Close", null).show();
        }
        else {
            new GetXML().execute("");
        }

        widget.addDecorator(oneDayDecorator);
        widget.setOnDateChangedListener(this);

        final Button todayButton = (Button) findViewById(R.id.todayButton);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date today = new Date();
                CalendarDay d = CalendarDay.from(today);
                widget.setCurrentDate(today);
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;

    }



    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        selectedDate = date;
        if(events.shouldDecorate(date)) {
            selectedEvents.clear();
            findEvents(date.getDate());
            String[] events = new String[selectedEvents.size()];
            for(int i = 0; i < selectedEvents.size(); i++) {
                Event e = selectedEvents.get(i);
                events[i] = e.getType();
                System.out.println(e.getType());
            }

            ListView eventListView = (ListView)findViewById(R.id.eventList);
            eventListView.setAdapter(new EventAdapter(P3_Main.this, events));
            eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Event e = selectedEvents.get(position);
                    Intent newsScreen = new Intent(P3_Main.this, EventActivity.class);
                    newsScreen.putExtra("data", e);
                    startActivity(newsScreen);
                }
            });
        }
        else {
            ListView eventListView = (ListView)findViewById(R.id.eventList);
            eventListView.setAdapter(null);
        }
    }

    public void findEvents(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = df.format(date);
        for(int i = 0; i < xmlEvents.size(); i++)  {
            Event e = xmlEvents.get(i);
            if(s.equals(e.getDate())) {
                selectedEvents.add(e);
            }
        }
    }

    private class GetXML extends AsyncTask<String, Void, String> {
        String src = null;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://branko-cirovic.appspot.com/etcapp/events.xml");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                src = readStream(con.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return src;
        }

        @Override
        protected void onPostExecute(String result) {
            xmlEvents = new ArrayList<Event>();
            xmlEvents.clear();

            parseXML(src);

            String old = readFileFromInternalStorage(filename); // local data
            StringBuffer b = new StringBuffer("<events>");
            b.append(old);
            b.append("</events>");

            System.out.println();

            parseXML(b.toString());

            int d, m, y;
            CalendarDay day;
            for(int i = 0; i < xmlEvents.size(); i++) {
                Event e = xmlEvents.get(i);
                String date = e.getDate();
                String[] a = date.split("-");
                y = Integer.parseInt(a[0]);
                m = Integer.parseInt(a[1]) - 1; // months start at 0 in Java
                d = Integer.parseInt(a[2]);
                day = CalendarDay.from(y,m,d);
                dates.add(day);
            }

            events = new EventDecorator(Color.BLACK, dates);
            widget.addDecorator(events);
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

    public static void parseXML(String src) {
        String date = new String();
        String type = new String();
        String description = new String();

        try {
            StringReader sr = new StringReader(src);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(sr);
            Event e;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("type")) {
                        eventType = xpp.next();
                        if(eventType == XmlPullParser.TEXT) {
                            type = xpp.getText();
                        }
                    }
                }

                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("date")) {
                        eventType = xpp.next();
                        if(eventType == XmlPullParser.TEXT) {
                            date = xpp.getText();
                        }
                    }
                }

                if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("description")) {
                        eventType = xpp.next();
                        if(eventType == XmlPullParser.TEXT) {
                            description = xpp.getText();
                        }
                    }
                }

                if(eventType == XmlPullParser.END_TAG) {
                    if(xpp.getName().equals("event")) {
                        e = new Event(type, date, description);
                        xmlEvents.add(e);
                    }
                }

                eventType = xpp.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar     // if it is present.

        getMenuInflater().inflate(R.menu.p3_main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        final CalendarDay d;
        final String ss;

        if(id == R.id.add) {
            if(selectedDate == null) {
                d = widget.getCurrentDate(); // first of month
            }
            else {
                d = selectedDate;
                selectedDate = null;
            }

            /*
            Toast.makeText(getBaseContext(), "Add Clicked " + d,
                    Toast.LENGTH_SHORT).show();
            */

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int sd = d.getDay();
            int sm = d.getMonth();
            int sy = d.getYear();

            Calendar c = Calendar.getInstance();
            c.set(sy, sm, sd, 0, 0);
            Date sDate = c.getTime();
            ss = sdf.format(sDate);

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.p3_dialog_layout, (ViewGroup) findViewById(R.id.layout_root));

            final EditText titleBox = (EditText) layout.findViewById(R.id.title);
            final EditText descriptionBox = (EditText) layout.findViewById(R.id.description);

            AlertDialog.Builder builder = new AlertDialog.Builder(P3_Main.this);
            builder.setView(layout);
            builder.setTitle("Date: " + ss);

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String title = titleBox.getText().toString();
                    String description = descriptionBox.getText().toString();
                    String src = "<event><type>" + title + "</type>" + "<date>" + ss + "</date>" + "<description>" + description + "</description></event>";
                    String old = readFileFromInternalStorage(filename);
                    StringBuffer b = new StringBuffer(old);
                    b.append(src);
                    writeFileToInternalStorage(filename, b.toString());

                    dates.add(d);
                    Event e = new Event(title, ss, description);
                    xmlEvents.add(e);
                    events = new EventDecorator(Color.BLACK, dates);
                    widget.addDecorator(events);

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        }
        return true;
    }

    public void writeFileToInternalStorage(String fileName, String data) {
        String eol = System.getProperty("line.separator");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE)));
            writer.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readFileFromInternalStorage(String fileName) {
        String eol = System.getProperty("line.separator");
        StringBuffer buffer = new StringBuffer();
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(openFileInput(fileName)));
            String line;
            while ((line = input.readLine()) != null) {
                buffer.append(line + eol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer.toString();
    }
}