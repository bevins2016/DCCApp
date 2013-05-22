package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
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
 * Created by Harmon on 5/17/13.
 */
public class ActionItemDetailFrag extends Fragment implements View.OnClickListener{

    ActionItem actionitem;
    TextView aisubject, aidate, aitime, aibody;
    Button aibutton;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.member_detail_layout, container, false);
        super.onCreate(savedInstanceState);

        actionitem = (ActionItem)this.getArguments().getSerializable("actionitem");

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

    @Override
    public void onClick(View view) {
        MemberDetailFragment detailFrag = new MemberDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("actionitem", actionitem);
        detailFrag.setArguments(bundle);

        FragmentManager manager = getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
        transaction.replace(R.id.fragmentcontainerright, detailFrag);

        transaction.commit();
    }
}
