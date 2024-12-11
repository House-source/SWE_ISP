package com.example.swe_isp.p3_calendar;

import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.swe_isp.R;

public class EventAdapter extends ArrayAdapter <String> {
    private Context context;
    private String[] items;

    public EventAdapter(Context context, String[] items) {
        super(context, R.layout.p3_eventrowlist, R.id.textid, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.p3_eventrowlist, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.textid);
        textView.setText(items[position]);
        return rowView;
    }
}