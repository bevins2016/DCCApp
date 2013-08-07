package com.S2013.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.S2013.dcc.R;
import com.S2013.dcc.helpers.ActionItem;
import com.S2013.dcc.helpers.OnButtonSelectedListener;
import com.S2013.dcc.helpers.mysql.MySQLQuery;

import java.util.concurrent.ExecutionException;

/**
 * This is fragment creates a detailed view of the action item details
 * Created by Harmon on 5/17/13.
 */

public class ActionItemDetailFrag extends Fragment implements View.OnClickListener{

    /**Action item to display*/
    private ActionItem actionitem;
    /**Static string of actionitem*/
    private static final String ACTIONITEM = "actionitem";
    /**Private on click listener*/
    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.action_item_detail_frag, container, false);
        super.onCreate(savedInstanceState);

        //Get the action item object
        actionitem = (ActionItem)getArguments().getSerializable(ACTIONITEM);

        //Get text items and set the text labels.
        TextView aisubject = (TextView) view.findViewById(R.id.aisubject);
        aisubject.setText(actionitem.getSubject());
        TextView aidate = (TextView) view.findViewById(R.id.aidate);
        aidate.setText(actionitem.getDate());
        TextView aitime = (TextView) view.findViewById(R.id.aitime);
        aitime.setText(actionitem.getTime());
        TextView aibody = (TextView) view.findViewById(R.id.aibody);
        aibody.setText(actionitem.getBody());

        Button aibutton = (Button) view.findViewById(R.id.aibutton);
        aibutton.setOnClickListener(this);
        if(actionitem.getStatus()==1) aibutton.setText("Delete Item");

        return view;
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

    /**
     * On click listener which will forward the fragment to allow the user to respond.
     * @param view
     */
    @Override
    public void onClick(View view) {
        if(actionitem.getStatus()==0){
            Log.e("Clicked", "Click detected..dfasdfasdfasd...now work?");
            ActionItemActionFrag detailFrag = new ActionItemActionFrag();

            Bundle bundle = new Bundle();
            bundle.putSerializable(ACTIONITEM, actionitem);
            detailFrag.setArguments(bundle);

            listener.launchFragment(detailFrag);
        }else{
            ActionItemFrag aifrag = new ActionItemFrag();
            try {
                new RemoveAiTask().execute().get();
                Toast.makeText(getActivity(), "Action Removed", Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            listener.launchFragment(aifrag);
        }
    }
    /**
     * Retrieves the list of members to from the database
     */
    private class RemoveAiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.e("dasd", "a;sdlkfaj;sdlf");
            MySQLQuery.removeActionItem("/DCC/removeActionItems.php?aid=", actionitem);
            return null;
        }
    }
}
