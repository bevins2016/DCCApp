package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.example.dcc.R;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static android.R.layout.*;

/**
 * This fragment is used by administrators to search edaily reports.
 *
 * This will send a string that will complete a PHP statement following
 * 'Select * From <TableName> WHERE '..
 *
 * Created by harmonbc on 5/23/13.
 */
public class AdminSearchFragment extends Fragment {
    List<User> members;
    Spinner spinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edaily_query,
                container, false);
        if((members = ObjectStorage.getMemberList())==null) {
            try {
                members = new GetMembersTask().get(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            ObjectStorage.setMemberList(members);
        }

        //Make a list of users names
        List<String> names = new ArrayList<String>();
        for(User m: members){
            names.add(m.getName());
        }
        spinner = (Spinner)view.findViewById(R.id.edailyspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                names);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        return view;
    }

    /**
     * This class is used to get the list of members if the value in the ObjectStorage class
     * is null.
     */
    public class GetMembersTask extends AsyncTask<Void, Void, List<User>> {
        @Override
        protected List<User> doInBackground(Void... params) {
            return MySQLQuery.getAllMembers("/DCC/getAllUsers.php");
        }


        @Override
        protected void onCancelled() {
        }
    }
}
