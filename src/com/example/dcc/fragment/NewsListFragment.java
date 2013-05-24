package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.dcc.R;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.mysql.HttpConnection;
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

    /*XML layout option*/
    private ListView listview;
    /*Adapter to append more news articles to the list*/
    private ArrayAdapter<Spanned> adapter;
    /*A List<E> of the news items that are pushed into the adapter*/
    private List<News> news;

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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsDetailFragment detailFrag = new NewsDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("news", news.get(i));
                detailFrag.setArguments(bundle);



                FragmentManager manager = getActivity().getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
                transaction.replace(R.id.fragmentcontainerright, detailFrag);

                transaction.commit();
            }
        });

        for(News n : news){
            adapter.add(Html.fromHtml(n.toString()));
        }
        return view;
    }


    public class GetNewsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            news  = MySQLQuery.getNews("/DCC/getNews.php");
            return true;
        }
    }

}
