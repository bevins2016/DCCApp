package com.example.dcc;

import android.util.Log;
import android.widget.*;
import com.example.dcc.helpers.ObjectStorage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;

public class BottomFragment extends Fragment implements View.OnClickListener{

	private ImageView userIcon;
    private TextView textView;
    private Button menuVisibility;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom_fragment,
				container, false);
		
		userIcon = (ImageView)view.findViewById(R.id.usericon);
        textView = (TextView)view.findViewById(R.id.userdata);

        menuVisibility = (Button)view.findViewById(R.id.menu_button);
        menuVisibility.setOnClickListener(this);

		userIcon.setImageBitmap(ObjectStorage.getUser().getImage());
        textView.setText(ObjectStorage.getUser().getName()+"("+ObjectStorage.getUser().getHandle()+")");

		return view;
	}

    @Override
    public void onClick(View view) {
        if(ObjectStorage.menuHidden){
            ObjectStorage.menuHidden = false;
            menuVisibility.setBackgroundResource(R.drawable.navigationpreviousitem);


            View menuV = ObjectStorage.getMenuFrame();
            ViewGroup.LayoutParams menuP = menuV.getLayoutParams();
            menuP.width = 200;
            menuV.setLayoutParams(menuP);
        }else{
            ObjectStorage.menuHidden = true;

            menuVisibility.setBackgroundResource(R.drawable.navigationnextitem);
            View menuV = ObjectStorage.getMenuFrame();
            ViewGroup.LayoutParams menuP = menuV.getLayoutParams();
            menuP.width = 1;
            menuV.setLayoutParams(menuP);
        }
    }
}
