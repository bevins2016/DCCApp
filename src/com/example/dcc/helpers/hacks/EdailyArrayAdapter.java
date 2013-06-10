package com.example.dcc.helpers.hacks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dcc.R;
import com.example.dcc.helpers.EDaily;

import java.util.List;

/**
 *
 * Created by harmonbc on 5/29/13.
 */
public class EdailyArrayAdapter extends ArrayAdapter<EDaily> {
    private final Context context;
    private final List<EDaily> eDailyList;


    public EdailyArrayAdapter(Context context, List<EDaily> edaily) {
        super(context, R.layout.edaily_item);
        this.context = context;
        this.eDailyList = edaily;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.edaily_item, parent, false);
        LinearLayout colorbox = (LinearLayout)rowView.findViewById(R.id.edailylinearLayout);
        TextView name = (TextView)rowView.findViewById(R.id.edailynamev);
        TextView date = (TextView)rowView.findViewById(R.id.edailydatev);

        EDaily edaily = eDailyList.get(position);
        name.setText(edaily.getLastname()+","+edaily.getFirstname());
        date.setText(edaily.getDate());

        int i = edaily.getGrade();

        if(i==-1) colorbox.setBackgroundColor(Color.BLACK);
        else if(i<=2){
            colorbox.setBackgroundColor(Color.RED);
        }else if(i<=5){
            colorbox.setBackgroundColor(Color.YELLOW);
        }else if(i<=13){
            colorbox.setBackgroundColor(Color.GREEN);
        }else{
            colorbox.setBackgroundColor(Color.BLUE);
        }

        return rowView;
    }
}
