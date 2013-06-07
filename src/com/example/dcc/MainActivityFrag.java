package com.example.dcc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.dcc.fragment.ActionItemFrag;
import com.example.dcc.fragment.AdminSearchFragment;
import com.example.dcc.fragment.CreateActionItemFrag;
import com.example.dcc.fragment.MembersListFragment;
import com.example.dcc.fragment.MenuFragment;
import com.example.dcc.fragment.NewsListFragment;
import com.example.dcc.fragment.TopFragment;
import com.example.dcc.helpers.ImageWithBool;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;

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
public class MainActivityFrag extends FragmentActivity implements OnButtonSelectedListener {

    public static final int LEFT_FRAG = R.id.fragmentcontainerleft;
    public static final int RIGHT_FRAG = R.id.fragmentcontainerright;
    public static final int BOTTOM_FRAG = R.id.fragmentcontainerbottom;
    Fragment menu, main, top;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_frag);


        if(savedInstanceState == null){
            FragmentManager manager = getFragmentManager();



            //Start transaction
            FragmentTransaction transaction = manager.beginTransaction();

            //Create fragments
            menu = new MenuFragment();
            main = new NewsListFragment();
            top = new TopFragment();

            //Add fragments
            transaction.add(RIGHT_FRAG,main);
            transaction.add(LEFT_FRAG,menu );
            transaction.add(BOTTOM_FRAG,top);

            transaction.commit();
        }
        //For convienence get the list of images early on... Slow connection propogation
        new GetImageUrlTask().execute();
    }

    @Override
    public void onMenuButtonSelected(int buttonId) {
        switch (buttonId) {
            case R.id.news:
                news();break;
            case R.id.photo:
                photo(); break;
            case R.id.report:
                report(); break;
            case R.id.search:
                search();break;
            case R.id.action:
                action(); break;
            case R.id.directory:
                directory();break;
            case R.id.logout:
                SharedPreferences mPreferences;
                mPreferences = this.getSharedPreferences("CurrentUser", 0);
                SharedPreferences.Editor editor=mPreferences.edit();
                editor.putString("UserName", "");
                editor.putString("PassWord", "");
                editor.commit();
                Toast.makeText(this, "Login data cleared",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.createaction:
                launchFragment(new CreateActionItemFrag()); break;
            case R.id.button:
                startVoiceRecognitionActivity(); break;
            case R.id.menu_button:
                displaySide(); break;
        }
    }

    private void displaySide() {
        //If menu is hidden, display the image
        if(ObjectStorage.menuHidden){
            ObjectStorage.menuHidden = false;
            FrameLayout menuFrame = (FrameLayout)findViewById(R.id.fragmentcontainerleft);
            menuFrame.setVisibility(View.VISIBLE);
        }else{
            //If the menu is visible, hide it
            ObjectStorage.menuHidden = true;
            //menuVisibility.setBackgroundResource(R.drawable.navigationnextitem);
            FrameLayout menuFrame = (FrameLayout)findViewById(R.id.fragmentcontainerleft);
            menuFrame.setVisibility(View.GONE);
        }
    }

    @Override
    public void launchFragment(Fragment newer){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(RIGHT_FRAG, newer);
        transaction.commit();
    }

    public void news(){
        launchFragment(new NewsListFragment());
    }

    public void photo(){
        Fragment newer = new CustomizedListViewFrag();
        launchFragment(newer);
    }
    public void report(){
        Fragment newer = new EReportLauncherFrag();
        launchFragment(newer);
    }
    public void search(){
        Fragment newer = new LaunchActivityFrag();
        launchFragment(newer);
    }
    public void action(){
        Fragment newer = new ActionItemFrag();
        launchFragment(newer);
    }
    public void directory(){
        Fragment newer = new MembersListFragment();
        launchFragment(newer);
    }
    public void searchReport(){
        Fragment newer = new AdminSearchFragment();
        launchFragment(newer);
    }
    public void toggle(){
    }

    public void createActionItems(){

        Fragment newer = new CreateActionItemFrag();
        launchFragment(newer);
    }

    /**
     * Fire an intent to start the speech recognition activity.
     */
    public void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speech recognition demo");
        startActivityForResult(intent, 1234);
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
