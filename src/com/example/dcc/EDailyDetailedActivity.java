package com.example.dcc;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.dcc.fragment.EDailyDetailFragment;
import com.example.dcc.helpers.EDaily;

/**
 * Created by harmonbc on 5/30/13.
 */
public class EDailyDetailedActivity extends FragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edaily_det_activity);

        EDaily edaily = (EDaily)getIntent().getSerializableExtra("edaily");

        FragmentManager manager = getFragmentManager();
        //Start transaction
        FragmentTransaction transaction = manager.beginTransaction();
        //Create fragments
        Fragment frag = new EDailyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("edaily", edaily);
        frag.setArguments(bundle);

        transaction.add(R.id.fragment, frag);
        transaction.commit();
    }
}