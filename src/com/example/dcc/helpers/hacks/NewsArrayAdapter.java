package com.example.dcc.helpers.hacks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dcc.R;
import com.example.dcc.helpers.EDaily;
import com.example.dcc.helpers.News;

import java.util.List;

/**
 *
 * Created by harmonbc on 5/29/13.
 */
public class NewsArrayAdapter extends ArrayAdapter<News> {
    private final Context context;
    private final List<News> newsList;


    public NewsArrayAdapter(Context context, List<News> news) {
        super(context, R.layout.news_item);
        this.context = context;
        this.newsList = news;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.news_item, parent, false);
        TextView title = (TextView)rowView.findViewById(R.id.newsItemTitle);
        TextView date = (TextView)rowView.findViewById(R.id.newsdate);
        TextView name = (TextView)rowView.findViewById(R.id.newsname);
        TextView body = (TextView)rowView.findViewById(R.id.newsbodyprev);
        ImageView icon = (ImageView)rowView.findViewById(R.id.newsimageView);

        News news = newsList.get(position);

        title.setText(news.getTitle());
        date.setText(news.getPubdate());
        name.setText(news.getPublisher().getName());
        String text = news.getText();
        if(text.length() > 100) text = text.substring(0,100);
        body.setText(text);

        icon.setImageBitmap(news.getPublisher().getImage());

        return rowView;
    }
}
