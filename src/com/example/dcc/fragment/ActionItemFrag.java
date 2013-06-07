package com.example.dcc.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;
import com.example.dcc.helpers.hacks.ActionArrayAdapter;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@SuppressLint("NewApi")
public class ActionItemFrag extends Fragment implements OnClickListener{

    ListView listview;

    ActionArrayAdapter adapter;
    List<ActionItem> actionItems;
    Activity activity;
    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

        View view = inflater.inflate(R.layout.action_item_fragment, container, false);
        activity = getActivity();

        GetNewsTask g = new GetNewsTask();
        try {
            g.execute((Void) null).get(20, TimeUnit.SECONDS);
            ObjectStorage.setActionItems(actionItems);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


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

        for(ActionItem n : actionItems){
            adapter.add(n);
        }

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

        }

        @Override
        protected void onCancelled() {
        }
    }





}
