package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.concurrent.ExecutionException;

/**
 * This is fragment creates a detailed view of the action item details
 * Created by Harmon on 5/17/13.
 */

//TODO: Move all the strings to the <class>Links</class> links.
public class ActionItemDetailFrag extends Fragment implements View.OnClickListener{

    private ActionItem actionitem;
    private static final String ACTIONITEM = "actionitem";

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

            FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
            transaction.replace(R.id.fragmentcontainerright, detailFrag);

            transaction.commit();
        }else{
            Log.e("Clicked", "Click detected...now work?");
            ActionItemFrag aifrag = new ActionItemFrag();

            FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            try {
                new RemoveAiTask().execute().get();
                Toast.makeText(getActivity(), "Action Removed", Toast.LENGTH_LONG).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            ObjectStorage.setFragment(R.id.fragmentcontainerright, aifrag);
            transaction.replace(R.id.fragmentcontainerright, aifrag);

            transaction.commit();
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
