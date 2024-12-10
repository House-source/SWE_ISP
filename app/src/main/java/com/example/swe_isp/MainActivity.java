package com.example.swe_isp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.swe_isp.p3_calendar.P3_Main;
import com.example.swe_isp.p1_programs.P1_Main;
import com.example.swe_isp.p2_schedule.P2_Main;
import com.example.swe_isp.p4_transit.P4_Main;
import com.example.swe_isp.p5_contacts.P5_Main;
import com.example.swe_isp.p6_news.P6_Main;
import com.example.swe_isp.p7_info.P7_Main;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);  // Set the menu layout

        // Find the buttons from the layout
        ImageButton button1 = findViewById(R.id.button_1);
        ImageButton button2 = findViewById(R.id.button_2);
        ImageButton button3 = findViewById(R.id.button_3);
        ImageButton button4 = findViewById(R.id.button_4);
        ImageButton button5 = findViewById(R.id.button_5);
        ImageButton button6 = findViewById(R.id.button_6);
        ImageButton button7 = findViewById(R.id.button_7);

        // Set click listeners to navigate to different activities
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P1_Main.class);
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P2_Main.class);
                startActivity(intent);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P3_Main.class);
                startActivity(intent);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P4_Main.class);
                startActivity(intent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P5_Main.class);
                startActivity(intent);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P6_Main.class);
                startActivity(intent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, P7_Main.class);
                startActivity(intent);
            }
        });
    }

}
