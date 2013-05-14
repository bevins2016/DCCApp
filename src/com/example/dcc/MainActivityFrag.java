package com.example.dcc;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;

public class MainActivityFrag extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_frag);
		FragmentManager manager = getFragmentManager();
		
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fragmentcontainerleft, new MenuFragment());
		transaction.add(R.id.fragmentcontainerright, new DefaultFragment());
		transaction.add(R.id.fragmentcontainerbottom, new BottomFragment());
		transaction.commit();
	}

}
