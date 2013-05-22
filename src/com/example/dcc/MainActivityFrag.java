package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import com.example.dcc.helpers.ObjectStorage;

public class MainActivityFrag extends FragmentActivity {

    public static final int LEFT_FRAG = R.id.fragmentcontainerleft;
    public static final int RIGHT_FRAG = R.id.fragmentcontainerright;
    public static final int BOTTOM_FRAG = R.id.fragmentcontainerbottom;
    private Button menuVisibility;
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_frag);
        FragmentManager manager = getFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        Fragment menu = new MenuFragment();
        Fragment news = (ObjectStorage.getHashMap().containsKey(RIGHT_FRAG)) ?
                 ObjectStorage.getFragment(RIGHT_FRAG):new NewsListFragment();
        Fragment bottom = new BottomFragment();

        ObjectStorage.setFragment(LEFT_FRAG, menu);
        ObjectStorage.setFragment(RIGHT_FRAG, news);
        ObjectStorage.setFragment(BOTTOM_FRAG, bottom);

        //Hack to allow for the management of the left frame. Need to access object form this view in
        //other views.
        ObjectStorage.setMenuFrame(findViewById(R.id.fragmentcontainerleft));

        transaction.add(RIGHT_FRAG,news);
        transaction.add(LEFT_FRAG,menu );
        transaction.add(BOTTOM_FRAG,bottom);

        transaction.commit();
    }
}
