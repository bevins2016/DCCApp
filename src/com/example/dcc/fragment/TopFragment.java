package com.example.dcc.fragment;

import android.widget.*;
import com.example.dcc.R;
import com.example.dcc.helpers.ObjectStorage;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Just Kidding, this is actually the top fragment
 */
public class TopFragment extends Fragment implements View.OnClickListener{

    //Toggles the menu visibility
    private Button menuVisibility;
    //Menu Width
    private static final int WIDTH = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_fragment,
                container, false);

        //Get needed fields
        ImageView userIcon = (ImageView) view.findViewById(R.id.usericon);
        TextView textView = (TextView) view.findViewById(R.id.userdata);
        menuVisibility = (Button)view.findViewById(R.id.menu_button);

        //Set listener
        menuVisibility.setOnClickListener(this);

        //Set unique data
        userIcon.setImageBitmap(ObjectStorage.getUser().getImage());
        textView.setText(ObjectStorage.getUser().getName() + "(" + ObjectStorage.getUser().getHandle() + ")");

        return view;
    }

    /**Take action on the button press*/
    @Override
    public void onClick(View view) {

        //If menu is hidden, display the image
        if(ObjectStorage.menuHidden){
            ObjectStorage.menuHidden = false;
            menuVisibility.setBackgroundResource(R.drawable.navigationpreviousitem);
            View menuV = ObjectStorage.getMenuFrame();
            ViewGroup.LayoutParams menuP = menuV.getLayoutParams();
            menuP.width = WIDTH;
            menuV.setLayoutParams(menuP);

        }else{
            //If the menu is visible, hide it
            ObjectStorage.menuHidden = true;
            menuVisibility.setBackgroundResource(R.drawable.navigationnextitem);
            View menuV = ObjectStorage.getMenuFrame();
            ViewGroup.LayoutParams menuP = menuV.getLayoutParams();
            menuP.width = 1;
            menuV.setLayoutParams(menuP);
        }

    }
}
