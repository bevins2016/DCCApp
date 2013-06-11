package com.example.dcc.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.Toast;

import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.OnButtonSelectedListener;
import com.example.dcc.helpers.hacks.ActionArrayAdapter;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.List;



@SuppressLint("NewApi")
public class ActionItemFrag extends Fragment implements OnClickListener{

    private ListView listview;

    private ActionArrayAdapter adapter;
    private List<ActionItem> actionItems;
    private Activity activity;
    private Context context;
    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.action_item_fragment, container, false);
        activity = getActivity();

        GetNewsTask g = new GetNewsTask();

        g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        listview = (ListView)view.findViewById(R.id.ailistView);
        adapter = new ActionArrayAdapter(getActivity(), actionItems);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ActionItemDetailFrag detailFrag = new ActionItemDetailFrag();
                Bundle bundle = new Bundle();
                bundle.putSerializable("actionitem", actionItems.get(i));
                detailFrag.setArguments(bundle);

                listener.launchFragment(detailFrag);
            }
        });

        return view;
    }


    private void addToAdapter(List<ActionItem> list){
        if(adapter.getCount()>0) adapter.clear();
        if(list == null || actionItems.isEmpty()) {
            Toast.makeText(activity, "You have no action items.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        for(ActionItem n : list){
            adapter.add(n);
        }
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

    @Override
    public void onClick(View view) {

    }


    public class GetNewsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            actionItems  = MySQLQuery.getActionItems("/DCC/getActionItems.php");
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            addToAdapter(actionItems);
        }

        @Override
        protected void onCancelled() {
        }
    }





}
