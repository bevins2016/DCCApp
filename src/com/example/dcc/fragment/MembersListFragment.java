package com.example.dcc.fragment;

import android.app.Fragment;
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

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class MembersListFragment extends Fragment implements OnClickListener{

    ListView listview;

    ArrayAdapter<Spanned> adapter;
    List<User> members;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        View view = inflater.inflate(R.layout.members_list, container, false);

        if((members = ObjectStorage.getMemberList())==null){

            GetMembersTask t =  new GetMembersTask();
            t.execute((Void) null);
            try {
                t.get(20, TimeUnit.SECONDS);
                ObjectStorage.setMemberList(members);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<Spanned>(getActivity(), R.layout.member_item);
        listview = (ListView)view.findViewById(R.id.membersList);
        ObjectStorage.setMemberList(members);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                members.get(i).launchWindow(getActivity());
            }
        });

        for(User m : members){
            adapter.add(Html.fromHtml(m.toString()));
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        List<String> memberList = ObjectStorage.getMemberActivity();
        Log.e("Something", "Saving State");
        if(!members.isEmpty()){
            memberList.clear();
            savedInstanceState.clear();
            for(User m : members){
                memberList.add(m.getHandle());
                savedInstanceState.putSerializable(m.getHandle(), m);
            }
        }
    }

    @Override
    public void onClick(View v) {
    }

    public class GetMembersTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            members  = HttpConnection.getMembers();
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
