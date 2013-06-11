package com.example.dcc;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.dcc.fragment.ActionItemDetailFrag;
import com.example.dcc.fragment.ActionItemFrag;
import com.example.dcc.fragment.AdminSearchFragment;
import com.example.dcc.fragment.CreateActionItemFrag;
import com.example.dcc.fragment.MemberDetailFragment;
import com.example.dcc.fragment.MembersListFragment;
import com.example.dcc.fragment.MenuFragment;
import com.example.dcc.fragment.NewsDetailFragment;
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
public class MainActivityFrag extends FragmentActivity implements OnButtonSelectedListener{

    public static final int LEFT_FRAG = R.id.fragmentcontainerleft;
    public static final int RIGHT_FRAG = R.id.fragmentcontainerright;
    public static final int BOTTOM_FRAG = R.id.fragmentcontainerbottom;
    Fragment menu, main, top;

    private boolean isNews;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_frag);

        isNews = true;

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
                isNews = true;
                news();break;
            case R.id.photo:
                isNews = false;
                photo(); break;
            case R.id.report:
                isNews = false;
                report(); break;
            case R.id.search:
                isNews = false;
                search();break;
            case R.id.action:
                isNews = false;
                action(); break;
            case R.id.directory:
                isNews = false;
                directory();break;
            case R.id.logout:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                SharedPreferences mPreferences;
                                mPreferences = getApplicationContext().getSharedPreferences("CurrentUser", 0);
                                SharedPreferences.Editor editor=mPreferences.edit();
                                editor.putString("UserName", "");
                                editor.putString("PassWord", "");
                                editor.commit();
                                Toast.makeText(getApplicationContext(), "Login data cleared",
                                        Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(getApplicationContext(), "Login data not cleared",
                                        Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            case R.id.createaction:
                isNews = false;
                launchFragment(new CreateActionItemFrag()); break;
            case R.id.button:
                 break;
            case R.id.menu_button:
                displaySide(); break;
        }
    }

    private void displaySide() {
        //If menu is hidden, display the image
        if(ObjectStorage.menuHidden){
            ObjectStorage.menuHidden = false;
            ScrollView scrollview = (ScrollView)findViewById(R.id.fragleft);
            scrollview.setVisibility(View.VISIBLE);
            FrameLayout menuFrame = (FrameLayout)findViewById(R.id.fragmentcontainerleft);
            menuFrame.setVisibility(View.VISIBLE);
        }else{
            //If the menu is visible, hide it
            ObjectStorage.menuHidden = true;
            //menuVisibility.setBackgroundResource(R.drawable.navigationnextitem);
            ScrollView scrollview = (ScrollView)findViewById(R.id.fragleft);
            scrollview.setVisibility(View.GONE);
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

    public void stackFragment(Fragment newer){
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(RIGHT_FRAG, newer);
        transaction.commit();
    }
    public void news(){
        launchFragment(new NewsListFragment());
    }
    public void photo(){
        launchFragment(new CustomizedListViewFrag());
    }
    public void report(){
        launchFragment(new EDailyActivityFrag());
    }
    public void search(){
        launchFragment(new LaunchActivityFrag());
    }
    public void action(){
        launchFragment(new ActionItemFrag());
    }
    public void directory(){
        launchFragment(new MembersListFragment());
    }
    public void searchReport(){
        launchFragment(new AdminSearchFragment());
    }


    public void createActionItems(){
        Fragment newer = new CreateActionItemFrag();
        launchFragment(newer);
    }

    @Override
    public void onBackPressed(){
        if(!ObjectStorage.menuHidden) displaySide();
        else
        {
            FragmentManager fragmentManager = getFragmentManager();
            Fragment frag = fragmentManager.findFragmentById(RIGHT_FRAG);

            if(frag instanceof MemberDetailFragment){
                launchFragment(new MembersListFragment());
            }else if (frag instanceof ActionItemDetailFrag){
                launchFragment(new ActionItemFrag());
            }else if (frag instanceof NewsDetailFragment){
                launchFragment(new NewsListFragment());
            }
        }
    }



    private class GetImageUrlTask extends AsyncTask<Void, Void, List<ImageWithBool>> {

        @Override
        protected List<ImageWithBool> doInBackground(Void... voids) {
            List<ImageWithBool> imageList = new ArrayList<ImageWithBool>();
            //No need for cookies here (Ye Pictures are public)... nom nom nom
            try {
                Document doc = Jsoup.connect(
                        "http://www.virtualdiscoverycenter.net/media/photos/").get();
                Elements imgs = doc.getElementsByTag("img");
                for(int i = 0; i < imgs.size(); i++){
                    String url = imgs.get(i).attr("src");
                    //Images from this url are utility icons
                    if(url.startsWith("http://www.virtualdiscoverycenter.net/wp-content/gallery")){
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
