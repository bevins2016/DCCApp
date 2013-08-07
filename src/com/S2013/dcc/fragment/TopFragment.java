package com.S2013.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.S2013.dcc.R;
import com.S2013.dcc.helpers.OnButtonSelectedListener;

/**
 * Just Kidding, this is actually the top fragment
 */
public class TopFragment extends Fragment implements View.OnClickListener{

    //Toggles the menu visibility
    private Button menuVisibility;
    //Menu Width
    private OnButtonSelectedListener listener;

    private GestureOverlayView.OnGestureListener gesture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_fragment,
                container, false);

        //Get needed fields        TextView textView = (TextView) view.findViewById(R.id.userdata);
        menuVisibility = (Button)view.findViewById(R.id.menu_button);

        //Set listener
        menuVisibility.setOnClickListener(this);

        //Set unique data
        //userIcon.setImageBitmap(Storage.getUser().getImage());

        return view;
    }

    /**Take action on the button press*/
    @Override
    public void onClick(View view) {
            listener.onMenuButtonSelected(view.getId());
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        if(activity instanceof OnButtonSelectedListener){
            listener = (OnButtonSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() +
                    "must implement MyListFragment.OnButtonSelectedListener");
        }
    }
}
