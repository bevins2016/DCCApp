/**
 * This class manages the fragment responsible for displaying the media items (Pictures)
 */
package com.example.dcc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.dcc.fragment.NewsDetailFragment;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.hacks.AlbumArrayAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CustomizedListViewFrag extends Fragment {

    List<String> urls ;
    AlbumArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.albumview,
                container, false);
        //setRetainInstance(true);
        getURLS();
        GridView gridview = (GridView)view.findViewById(R.id.gridview);
        adapter = new AlbumArrayAdapter(getActivity(),  urls);
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), urls.get(i), Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    /**
     * Calls on the task that fetches a list of relevant images from inside of the page
     */
    private void getURLS() {
        try {
            if(ObjectStorage.getImageList()!=null)
            {
                urls = ObjectStorage.getImageList();
                return;
            }
            urls = new GetImageUrlTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class is responsible for fetching the list of url's using JSOUP.
     */
    private class GetImageUrlTask extends AsyncTask<Void, Void, List<String>>{

        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> imageList = new ArrayList<String>();
            //No need for cookies here (Ye Pictures are public)... nom nom nom
            try {
                if(ObjectStorage.getImageList()!=null) return ObjectStorage.getImageList();
                Document doc = Jsoup.connect(
                        "http://www.virtualdiscoverycenter.net/media/photos/").get();

                Elements imgs = doc.getElementsByClass("rgg_imagegrid ").first().getElementsByTag("a");
                for(int i = 0; i < imgs.size(); i++){
                    String url = imgs.get(i).attr("href");
                    //Images from this url are utility icons
                    if(url.startsWith("http://www.virtualdiscoverycenter.net/wp-content/uploads/2013")){
                        imageList.add(url);
                        Log.e("Images", "Added: "+url);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imageList;
        }
    }
}