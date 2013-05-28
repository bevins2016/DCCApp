package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.dcc.R;
import com.example.dcc.helpers.Member;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.HttpConnection;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.ArrayList;
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

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.members_list, container, false);

        //If the members object is null in the list, refresh the list
        if((members = ObjectStorage.getMemberList())==null){
            GetMembersTask t =  new GetMembersTask();
            t.execute((Void) null);
            try {
                members = t.get(20, TimeUnit.SECONDS);
                ObjectStorage.setMemberList(members);
            } catch (InterruptedException e) {
                Log.e(LOG, e.getMessage());
            } catch (ExecutionException e) {
                Log.e(LOG, e.getMessage());
            } catch (TimeoutException e) {
                Log.e(LOG, e.getMessage());
            }
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

                //Launch the fragment activity
                FragmentManager manager = getActivity().getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();

                ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
                transaction.replace(R.id.fragmentcontainerright, detailFrag);

                transaction.commit();
            }
        });

        //Add all members to the adapter
        for(User m : members){
            adapter.add(Html.fromHtml(m.toString()));
        }

        return view;
    }

    /**
     * Retrieves the list of members to from the database
     */
    public class GetMembersTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... params) {
            return MySQLQuery.getAllMembers("/DCC/getAllUsers.php");
        }
    }

}