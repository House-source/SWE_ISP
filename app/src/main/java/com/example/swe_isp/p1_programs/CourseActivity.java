package com.example.swe_isp.p1_programs;

/**
 * Created by admin on 2018-07-19.
 */

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.swe_isp.R;

public class CourseActivity extends AppCompatActivity {
    private String cid, cname, credit, lect, lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_course);

        Bundle extras = getIntent().getExtras();
        cid = extras.getString("cid");
        cname = extras.getString("cname");
        credit = extras.getString("credit");
        lect = extras.getString("lect");
        lab = extras.getString("lab");

        this.setTitle(cid);

        TextView cnt = (TextView)findViewById(R.id.cname);
        cnt.setText(cname);

        TextView crt = (TextView)findViewById(R.id.credit);
        crt.setText(credit + " credit hours");

        TextView let = (TextView)findViewById(R.id.lect);
        let.setText(lect + " hours per week");

        TextView lbt = (TextView)findViewById(R.id.lab);
        lbt.setText(lab + " hours per week");
    }
}