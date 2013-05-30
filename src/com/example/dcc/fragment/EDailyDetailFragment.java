package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dcc.R;
import com.example.dcc.helpers.EDaily;

/**
 * Created by harmonbc on 5/30/13.
 */
public class EDailyDetailFragment extends Fragment {
    private TextView author;
    private TextView proj;
    private TextView submittedon;
    private TextView datefor;
    private TextView iss;
    private TextView rel;
    private TextView dep;
    private TextView grade;
    private TextView body;

    EDaily edaily;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.edaily_detailed, container, false);
        super.onCreate(savedInstanceState);

        Log.e("Fragment", "edailydetailfrag is alive!");
        edaily = (EDaily) getArguments().getSerializable("edaily");

        author = (TextView)view.findViewById(R.id.edailydetname);
        proj = (TextView)view.findViewById(R.id.edailydetproj);
        submittedon = (TextView)view.findViewById(R.id.edailydetdatesub);
        datefor = (TextView)view.findViewById(R.id.edailydetdatefor);
        dep = (TextView)view.findViewById(R.id.edailydetdeps);
        rel = (TextView)view.findViewById(R.id.edailydetrelscore);
        iss = (TextView)view.findViewById(R.id.edailyissscore);
        grade = (TextView)view.findViewById(R.id.edailydetgrade);
        body = (TextView)view.findViewById(R.id.edailydetbody);

        author.setText(edaily.getLastname()+","+edaily.getFirstname());
        proj.setText(edaily.getProj());
        submittedon.setText(edaily.getSubmitted());
        datefor.setText(edaily.getDate());
        dep.setText(""+edaily.getDep());
        rel.setText(""+edaily.getRel());
        iss.setText(""+edaily.getIss());
        grade.setText(""+edaily.getGrade());
        body.setText(edaily.getBody());

        return view;
    }

}
