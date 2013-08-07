package com.example.dcc.helpers.hacks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.News;

import java.util.List;

/**
 * Created by harmonbc on 5/30/13.
 */
public class ActionArrayAdapter extends ArrayAdapter<ActionItem> {
    private final Context context;
    private final List<ActionItem> actionitems;


    public ActionArrayAdapter(Context context, List<ActionItem> actionitems) {
        super(context, R.layout.action_item);
        this.context = context;
        this.actionitems = actionitems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.action_item, parent, false);
        TextView desc = (TextView)rowView.findViewById(R.id.actionitemdescription);
        TextView date = (TextView)rowView.findViewById(R.id.actionitemdate);
        TextView time = (TextView)rowView.findViewById(R.id.actionitemstime);
        ImageView icon = (ImageView)rowView.findViewById(R.id.actionitemimageView);

        ActionItem actionitem = actionitems.get(position);

        String description = actionitem.getDescription();
        if(description.length() >100) description = description.substring(0,100);

        desc.setText(description);
        date.setText(actionitem.getDate());
        time.setText(actionitem.getTime());

        if(actionitem.getStatus() == 0) icon.setImageResource(R.drawable.warn);
        else icon.setImageResource(R.drawable.check);

        return rowView;
    }
}
