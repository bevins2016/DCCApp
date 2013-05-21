package com.example.dcc;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.Inflater;

import android.text.Html;
import android.text.Spanned;
import com.example.dcc.R;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.mysql.HttpConnection;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class NewsListFragment extends Fragment implements OnClickListener{

    ListView listview;

    ArrayAdapter<Spanned> adapter;
    List<News> news;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.news_list_fragment, container, false);

        if((news = ObjectStorage.getNewsList()) == null){
            GetNewsTask g = new GetNewsTask();
            g.execute((Void) null);
            try {
                g.get(10, TimeUnit.SECONDS);
                ObjectStorage.setNewsList(news);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }


        listview = (ListView)view.findViewById(R.id.newslist);
        adapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.news_item);
        listview.setAdapter(adapter);

        for(News n : news){
            adapter.add(Html.fromHtml(n.toString()));
        }
        return view;
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    public class GetNewsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            news  = HttpConnection.getNews();
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {
        }
    }

}
