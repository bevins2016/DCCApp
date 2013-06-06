package com.example.dcc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.dcc.fragment.MenuFragment;
import com.example.dcc.fragment.NewsListFragment;
import com.example.dcc.fragment.TopFragment;
import com.example.dcc.helpers.ImageWithBool;
import com.example.dcc.helpers.ObjectStorage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This activity manages all fragments attached to it for the durration of the life of the
 * application.
 */
public class MainActivityFrag extends FragmentActivity {

    public static final int LEFT_FRAG = R.id.fragmentcontainerleft;
    public static final int RIGHT_FRAG = R.id.fragmentcontainerright;
    public static final int BOTTOM_FRAG = R.id.fragmentcontainerbottom;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_frag);
        FragmentManager manager = getFragmentManager();

        //For convienence get the list of images early on... Slow connection propogation
        new GetImageUrlTask().execute();

        //Start transaction
        FragmentTransaction transaction = manager.beginTransaction();

        //Create fragments
        Fragment menu = new MenuFragment();
        Fragment news = (ObjectStorage.getHashMap().containsKey(RIGHT_FRAG)) ?
                 ObjectStorage.getFragment(RIGHT_FRAG):new NewsListFragment();
        Fragment bottom = new TopFragment();

        //Set fragments
        ObjectStorage.setFragment(LEFT_FRAG, menu);
        ObjectStorage.setFragment(RIGHT_FRAG, news);
        ObjectStorage.setFragment(BOTTOM_FRAG, bottom);

        //Hack to allow for the management of the left frame. Need to access object form this view in
        //other views.
        ObjectStorage.setMenuFrame(findViewById(R.id.fragmentcontainerleft));

        //Add fragments
        transaction.add(RIGHT_FRAG,news);
        transaction.add(LEFT_FRAG,menu );
        transaction.add(BOTTOM_FRAG,bottom);

        transaction.commit();
    }

    private class GetImageUrlTask extends AsyncTask<Void, Void, List<ImageWithBool>> {

        @Override
        protected List<ImageWithBool> doInBackground(Void... voids) {
            HashMap<String, Boolean> hashMap = new HashMap<String, Boolean>();

            List<ImageWithBool> imageList = new ArrayList<ImageWithBool>();
            //No need for cookies here (Ye Pictures are public)... nom nom nom
            try {
                Document doc = Jsoup.connect(
                        "http://www.virtualdiscoverycenter.net/media/photos/").get();
                Elements imgs = doc.getElementsByTag("img");
                for(int i = 0; i < imgs.size(); i++){
                    String url = imgs.get(i).attr("src");
                    //Images from this url are utility icons
                    if(url.startsWith("http://www.virtualdiscoverycenter.net/wp-content/uploads/")){
                        String url2;
                        try{
                        url2 = url.substring(url.lastIndexOf("/"));
                        String url3 = url2.substring(0, url2.indexOf("-"));
                            if(hashMap.containsKey(url3)) break;
                            else hashMap.put(url3, true);
                        }catch(Exception e){
                            Log.e("ARGH2", "These are not the droid you're looking for");
                        }
                        ImageWithBool temp = new ImageWithBool();
                        temp.url = url;
                        imageList.add(temp);
                    }
                }

                ObjectStorage.setImageList(imageList);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return imageList;
        }
    }
}
