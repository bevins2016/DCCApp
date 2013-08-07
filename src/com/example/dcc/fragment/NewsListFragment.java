package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dcc.R;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;
import com.example.dcc.helpers.hacks.NewsArrayAdapter;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Used to display a list of aritlces from the VDC site.
 *
 * @author Brandon Harmon
 */
public class NewsListFragment extends Fragment{

    private NewsArrayAdapter adapter;
    private ListView listview;
    /*A List<E> of the news items that are pushed into the adapter*/
    private List<News> news;
    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.news_list_fragment, container, false);

        //Get new news list if static object is null
        if((news = ObjectStorage.getNewsList()) == null){
            GetNewsTask g = new GetNewsTask();
            g.execute((Void) null);
            try {
                g.get(20, TimeUnit.SECONDS);
                ObjectStorage.setNewsList(news);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        adapter = new NewsArrayAdapter(getActivity(), news);

        listview = (ListView)view.findViewById(R.id.newslist);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsDetailFragment detailFrag = new NewsDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", news.get(i));
                detailFrag.setArguments(bundle);
                listener.launchFragment(detailFrag);
            }
        });

        try{
            for(News n : news){
                adapter.add(n);
            }
        }catch(NullPointerException e){
            Toast.makeText(getActivity(), "Connection Timed Out", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof OnButtonSelectedListener){
            listener = (OnButtonSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() +
                    "must implement MyListFragment.OnButtonSelectedListener");
        }
    }

    /*
     *Retrives the newslist from the mysql database
     */
    public class GetNewsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            news  = MySQLQuery.getNews("/DCC/getNews.php");
            return true;
        }
    }
}
