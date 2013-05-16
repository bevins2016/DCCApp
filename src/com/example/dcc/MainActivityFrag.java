package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import com.example.dcc.helpers.ObjectStorage;

public class MainActivityFrag extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_frag);
		FragmentManager manager = getFragmentManager();
		
		FragmentTransaction transaction = manager.beginTransaction();
        Fragment menu = new MenuFragment();
        Fragment news = new NewsListFragment();
        Fragment bottom = new BottomFragment();

        ObjectStorage.setFragment(R.id.fragmentcontainerleft, menu);
        ObjectStorage.setFragment(R.id.fragmentcontainerright, news);
        ObjectStorage.setFragment(R.id.fragmentcontainerbottom, bottom);

        transaction.add(R.id.fragmentcontainerleft,menu );
		transaction.add(R.id.fragmentcontainerright,news);
		transaction.add(R.id.fragmentcontainerbottom,bottom);
		transaction.commit();
	}

}
