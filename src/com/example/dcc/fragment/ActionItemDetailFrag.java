package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.Member;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;

/**
 * This is fragment creates a detailed view of the action item details
 * Created by Harmon on 5/17/13.
 */

//TODO: Move all the strings to the <class>Links</class> links.
public class ActionItemDetailFrag extends Fragment implements View.OnClickListener{

    private ActionItem actionitem;
    private TextView aisubject, aidate, aitime, aibody;
    private Button aibutton;
    private static final String ACTIONITEM = "actionitem";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.action_item_detail_frag, container, false);
        super.onCreate(savedInstanceState);

        //Get the action item object
        actionitem = (ActionItem)getArguments().getSerializable(ACTIONITEM);

        //Get text items and set the text labels.
        aisubject = (TextView)view.findViewById(R.id.aisubject);
        aisubject.setText(actionitem.getSubject());
        aidate = (TextView)view.findViewById(R.id.aidate);
        aidate.setText(actionitem.getDate());
        aitime = (TextView)view.findViewById(R.id.aitime);
        aitime.setText(actionitem.getTime());
        aibody = (TextView) view.findViewById(R.id.aibody);
        aibody.setText(actionitem.getBody());

        aibutton = (Button) view.findViewById(R.id.aibutton);
        aibutton.setOnClickListener(this);

        return view;
    }

    /**
     * On click listener which will forward the fragment to allow the user to respond.
     * @param view
     */
    @Override
    public void onClick(View view) {
        ActionItemActionFrag detailFrag = new ActionItemActionFrag();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACTIONITEM, actionitem);
        detailFrag.setArguments(bundle);

        FragmentManager manager = getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
        transaction.replace(R.id.fragmentcontainerright, detailFrag);

        transaction.commit();
    }
}
