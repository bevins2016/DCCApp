package com.example.dcc;

import android.app.Activity;
import android.app.ListFragment;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the
 * ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MainListFragmentFragment extends ListFragment {
	OnButtonSelectedListener mCallBack;

	public interface OnButtonSelectedListener {
		public void onArticleSelected(int position);
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
        try {
        	mCallBack = (OnButtonSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
	}
}

