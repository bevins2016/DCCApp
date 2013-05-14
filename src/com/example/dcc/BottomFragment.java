package com.example.dcc;

import com.example.dcc.helpers.ObjectStorage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;

public class BottomFragment extends Fragment{

	private ImageView userIcon;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_fragment,
				container, false);
		
		userIcon = (ImageView)view.findViewById(R.id.usericon);
		userIcon.setImageBitmap(ObjectStorage.getUser().getImage());
		userIcon.setScaleX((float)2.0);
		userIcon.setScaleY((float)2.0);
		return view;
	}
}
