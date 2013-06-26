package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dcc.R;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This fragment lists all of the members in the VDC database in a directory
 * value.
 * @author Brandon Harmon
 */

public class MembersListFragment extends Fragment{

    //Listview of strings
    private ListView listview;
    //Adapter for the listview
    private ArrayAdapter<Spanned> adapter;
    //List of members that are pushed into the adapter for the list
    private List<User> members;
    //Name of this class in the logs
    private static final String LOG = "dcc.MemberListFragment";

    private OnButtonSelectedListener listener;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.members_list, container, false);

        boolean waitToLoad = false;
        //If the members object is null in the list, refresh the list
        if((members = ObjectStorage.getMemberList())==null){
            waitToLoad = true;
            GetMembersTask t =  new GetMembersTask();
            t.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        //Make an adapter
        adapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.member_item);

        //Get the listview, set the listview and the adapter
        listview = (ListView)view.findViewById(R.id.membersList);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Get the corrosponding item
                User member =  members.get(i);
                //Make the fragment that will be launched
                MemberDetailFragment detailFrag = new MemberDetailFragment();
                //Place the member to be displayed into a bundle
                Bundle bundle = new Bundle();
                bundle.putSerializable("member", member);
                detailFrag.setArguments(bundle);

                listener.launchFragment(detailFrag);
            }
        });

        if(!waitToLoad){
            for(User m : members){
                adapter.setNotifyOnChange(true);
                adapter.add(Html.fromHtml(m.toString()));
            }
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

    /**
     * Retrieves the list of members to from the database
     */
    public class GetMembersTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            members =  MySQLQuery.getAllMembers("/DCC/getAllUsers.php");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(null);
            Collections.sort(members);
            ObjectStorage.setMemberList(members);

            try{
                //Add all members to the adapter
                for(User m : members){
                    adapter.setNotifyOnChange(true);
                    adapter.add(Html.fromHtml(m.toString()));
                }
            }catch(NullPointerException e){
                Toast.makeText(getActivity(), "Failed to load friends.", Toast.LENGTH_LONG);
            }
        }
    }

}