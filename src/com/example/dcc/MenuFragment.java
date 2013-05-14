package com.example.dcc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MenuFragment extends Fragment implements android.view.View.OnClickListener{
	
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
			activity.startActivity(new Intent(activity, CustomizedListView.class));
			break;
		case R.id.report:
			activity.startActivity(new Intent(activity, EReportLauncher.class));
			break;
		case R.id.search:
			activity.startActivity(new Intent(activity, LaunchActivity.class));
			break;
		case R.id.action:
			activity.startActivity(new Intent(activity, ActionItem.class));
			break;
		case R.id.directory:
			activity.startActivity(new Intent(activity, MainActivity.class));
			break;
		}		
	}
}
