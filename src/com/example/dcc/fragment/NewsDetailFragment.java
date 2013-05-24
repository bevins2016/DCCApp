package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.dcc.R;
import com.example.dcc.helpers.News;

/**
 * Displays the full body of the news object in a fragment
 * Created by harmonbc on 5/21/13.
 */
public class NewsDetailFragment extends Fragment {

    News news;
    TextView newstitle, newsdate, newspublisher, newsBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(com.example.dcc.R.layout.news_detailed,
                container, false);
        news = (News)this.getArguments().getSerializable("news");

        //Get all text views
        newstitle = (TextView)view.findViewById(R.id.newstitle);
        newsdate = (TextView)view.findViewById(R.id.newsdate);
        newspublisher = (TextView)view.findViewById(R.id.newspublisher);
        newsBody = (TextView)view.findViewById(R.id.newsbody);

        //Populate text views with news details
        newstitle.setText(news.getTitle());
        newsdate.setText(news.getPubdate());
        newspublisher.setText(news.getPublisher());
        newsBody.setText(news.getText());

        return view;
    }
}
