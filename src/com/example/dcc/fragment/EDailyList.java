package com.example.dcc.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dcc.EDailyDetailedActivity;
import com.example.dcc.R;
import com.example.dcc.helpers.EDaily;
import com.example.dcc.helpers.hacks.DCCArrayList;
import com.example.dcc.helpers.hacks.EdailyArrayAdapter;

/**
 *
 * Created by harmonbc on 5/29/13.
 */
public class EDailyList extends Fragment {
    EdailyArrayAdapter adapter;
    ListView listview;
    DCCArrayList edailies;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edaily_list,
                container, false);
        edailies = (DCCArrayList)getArguments().getSerializable("edailies");

        //Make an adapter
        adapter = new EdailyArrayAdapter(getActivity(), edailies);

        //Get the listview, set the listview and the adapter
        listview = (ListView)view.findViewById(R.id.edailylistv);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Get the corrosponding item
                EDaily edaily = (EDaily) edailies.get(i);

                Intent intent = new Intent(getActivity(), EDailyDetailedActivity.class);
                intent.putExtra("edaily", edaily);
                getActivity().startActivity(intent);
            }
        });


      for(Object e : edailies){
            adapter.add(((EDaily)e));
        }
        return view;
    }
}
