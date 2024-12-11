package com.example.swe_isp.p3_calendar;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.swe_isp.R;

public class EventActivity extends AppCompatActivity {

    private String type = null;
    private String date = null;
    private String description = null;

    Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();

        e = (Event) extras.getSerializable("data");

        type = e.getType();
        date = e.getDate();
        description = e.getDescription();

        setContentView(R.layout.p3_event);
        TextView dview = (TextView) findViewById(R.id.date);
        TextView tview = (TextView) findViewById(R.id.type);
        TextView desview = (TextView) findViewById(R.id.description);

        dview.setText(format(date));
        tview.setText(type);

        description = description.replace("\\n", System.getProperty("line.separator"));
        desview.setSingleLine(false);
        desview.setText(description);
    }

    private String format(String date) {
        String result = new String();
        final String OLD_FORMAT = "yyyy-MM-dd";
        final String NEW_FORMAT = "EEEE, MMMM d, yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        try {
            Date d = sdf.parse(date);
            sdf.applyPattern(NEW_FORMAT);
            result = sdf.format(d);
        }
        catch(Exception e) {}

        return result;
    }
}
