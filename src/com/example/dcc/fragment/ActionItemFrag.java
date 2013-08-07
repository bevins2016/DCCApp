package com.example.dcc.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The Action Item frag is responsible for the listing all action items to the users.
 * This list utilizes the action item array adapter to display the action items correctly
 **/

@SuppressLint("NewApi")
public class ActionItemFrag extends Fragment {

    /*This is the adapter that displays each action item*/
    private ActionArrayAdapter adapter;
    /*List of action items that are added to the adapter*/
    private List<ActionItem> actionItems;
    /*Reference to the activity that calls the fragment*/
    private Activity activity;
    /*Implemented by the activity calling this fragment*/
    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.action_item_fragment, container, false);
        activity = getActivity();

        GetNewsTask g = new GetNewsTask();

        try {
            g.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        ListView listview = (ListView) view.findViewById(R.id.ailistView);
        adapter = new ActionArrayAdapter(getActivity(), actionItems);


        if(actionItems == null || actionItems.isEmpty()) {
            Toast.makeText(activity, "You have no action items.",
                    Toast.LENGTH_LONG).show();
        }else{
            listview.setAdapter(adapter);

            for(ActionItem n : actionItems){
                adapter.add(n);
            }
        }

        //Set the listener for every action in the action item list
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


    /*
     * This attaches the activty which implements the onButtonSelectedListener
     * to this fragment.
     */
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


    /**
     * sets the action item list
     * //TODO: Move this to the main activity and pass it in using a static class.
     * //This will batch small sized network requests together.
     */
    public class GetNewsTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            actionItems  = MySQLQuery.getActionItems("/DCC/getActionItems.php");
            return true;
        }
    }





}
