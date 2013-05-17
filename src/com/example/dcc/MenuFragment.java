package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.dcc.fragment.MembersListFragment;
import com.example.dcc.helpers.ObjectStorage;

public class MenuFragment extends Fragment implements OnClickListener{
	
	private Button newsB;
	private Button calB;
	private Button photoB;
	private Button reportB;
	private Button actionB;
	private Button directoryB;
	private Button searchB;
	
	private Activity activity;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_fragment,
				container, false);
		activity = getActivity();
		newsB = (Button) view.findViewById(R.id.news);
		calB = (Button) view.findViewById(R.id.calendar);
		photoB = (Button) view.findViewById(R.id.photo);
		reportB = (Button) view.findViewById(R.id.report);
		actionB = (Button) view.findViewById(R.id.action);
		directoryB = (Button) view.findViewById(R.id.directory);
		searchB = (Button) view.findViewById(R.id.search);

		newsB.setOnClickListener(this);
		calB.setOnClickListener(this);
		photoB.setOnClickListener(this);
		reportB.setOnClickListener(this);
		actionB.setOnClickListener(this);
		directoryB.setOnClickListener(this);
		searchB.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.news:
			getActivity().startActivity(new Intent(getActivity(), AndroidRssReader.class));
			break;
		case R.id.calendar:
			break;
		case R.id.photo:
			activity.startActivity(new Intent(activity, com.example.dcc.CustomizedListViewFrag.class));
			break;
		case R.id.report:
			activity.startActivity(new Intent(activity, EReportLauncher.class));
			break;
		case R.id.search:
			activity.startActivity(new Intent(activity, LaunchActivityFrag.class));
			break;
		case R.id.action:
//			activity.startActivity(new Intent(activity, ActionItemFrag.class));
            FragmentManager manager = activity.getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
            Fragment newer = new ActionItemFrag();
            ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

            transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
            transaction.commit();
			break;
		case R.id.directory:
             manager = activity.getFragmentManager();
             transaction = manager.beginTransaction();

             old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
             newer = new MembersListFragment();
            ObjectStorage.setFragment(R.id.fragmentcontainerright, newer);

            transaction.replace(R.id.fragmentcontainerright, ObjectStorage.getFragment(R.id.fragmentcontainerright));
            transaction.commit();
			break;
		}		
	}
}
