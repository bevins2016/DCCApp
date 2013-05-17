package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.dcc.R;
import com.example.dcc.helpers.Member;

/**
 * Created by Harmon on 5/17/13.
 */
public class MemberDetailFragment extends Fragment {

    Member member;
    TextView name;
    private TextView handle;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.member_detail_layout, container, false);
        super.onCreate(savedInstanceState);

        member = (Member)this.getArguments().getSerializable("member");

        name = (Button)view.findViewById(R.id.memname);
        handle = (Button) view.findViewById(R.id.memhandle);

        name.setText(member.getName());
        name.setText(member.getHandle());
        return view;
    }

}
