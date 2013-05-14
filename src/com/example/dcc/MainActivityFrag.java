package com.example.dcc;

import com.example.dcc.MainListFragmentFragment.OnButtonSelectedListener;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

public class MainActivityFrag extends Activity{
	private User user;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity_frag);
		FragmentManager manager = getFragmentManager();
		
		user = ObjectStorage.getUser();
		
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.fragmentcontainerleft, new MenuFragment());
		transaction.add(R.id.fragmentcontainerright, new DefaultFragment());
		
		transaction.commit();
	}

}
