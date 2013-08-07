package com.S2013.dcc.helpers.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.S2013.dcc.R;
import com.S2013.dcc.helpers.ActionItem;

import java.util.List;

/**
 * Used to manipulate the display of Action Items in the List View
 * Created by harmonbc on 5/30/13.
 */
public class ActionArrayAdapter extends ArrayAdapter<ActionItem> {
    /**Context of the parent activity*/
    private final Context context;
    /**List of action items*/
    private final List<ActionItem> actionitems;


    /** Constructor*/
    public ActionArrayAdapter(Context context, List<ActionItem> actionitems) {
        super(context, R.layout.action_item);
        this.context = context;
        this.actionitems = actionitems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = (view == null) ? inflater.inflate(R.layout.action_item, parent, false) : view;
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
