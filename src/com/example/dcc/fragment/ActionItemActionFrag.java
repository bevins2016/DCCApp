package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.ObjectStorage;

/**
 * Created by Harmon on 5/22/13.
 */
public class ActionItemActionFrag extends Fragment implements View.OnClickListener{

    ActionItem actionitem;
    TextView aitext;
    EditText userinput;
    Button aisubmit;
    Button aicancel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.action_item_action_frag, container, false);
        super.onCreate(savedInstanceState);

        actionitem = (ActionItem)this.getArguments().getSerializable("actionitem");

        aitext = (TextView)view.findViewById(R.id.aitext);
        aitext.setText(actionitem.getBody());


        return view;
    }

    @Override
    public void onClick(View view) {
        /*
        MemberDetailFragment detailFrag = new MemberDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("actionitem", actionitem);
        detailFrag.setArguments(bundle);

        FragmentManager manager = getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
        transaction.replace(R.id.fragmentcontainerright, detailFrag);

        transaction.commit();*/
    }
}
