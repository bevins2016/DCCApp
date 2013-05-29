package com.example.dcc.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.dcc.R;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


/**
 * This fragment is used by administrators to search edaily reports.
 *
 * This will send a string that will complete a PHP statement following
 * 'Select * From <TableName> WHERE '..
 *
 * Created by harmonbc on 5/23/13.
 */
public class AdminSearchFragment extends Fragment implements View.OnClickListener{
    List<User> members;
    Spinner spinner;
    Button search;
    EditText startDate, endDate, depNo, relNo, issNo;
    RadioGroup depRG, relRG, issRG;
    CheckBox issCB, relCB, depCB;
    SeekBar seekBar;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edaily_query,
                container, false);
<<<<<<< HEAD


        //Make a list of users names
=======
        getFields(view);
>>>>>>> 4dfec120dd511d589689e93e8ed2db1b12d2e143
        populateSpinner(view);
        return view;
    }

<<<<<<< HEAD
=======
    private void getFields(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        startDate = (EditText)view.findViewById(R.id.edailystartdate);
        endDate = (EditText)view.findViewById(R.id.edailyenddate);

        startDate.setText(sdf.format(new Date()));
        endDate.setText(sdf.format(new Date()));

        depNo = (EditText)view.findViewById(R.id.edailydepno);
        relNo = (EditText)view.findViewById(R.id.edailyrelno);
        issNo = (EditText)view.findViewById(R.id.edailyissno);

        issRG = (RadioGroup)view.findViewById(R.id.issrg);
        relRG = (RadioGroup)view.findViewById(R.id.relrg);
        depRG = (RadioGroup)view.findViewById(R.id.rgdep);

        issCB = (CheckBox)view.findViewById(R.id.edailyisseq);
        relCB = (CheckBox)view.findViewById(R.id.edailyreleq);
        depCB = (CheckBox)view.findViewById(R.id.edailydepeq);

        seekBar = (SeekBar)view.findViewById(R.id.scoreSeek);

        search = (Button)view.findViewById(R.id.edailysearch);
        search.setOnClickListener(this);
    }

>>>>>>> 4dfec120dd511d589689e93e8ed2db1b12d2e143
    private void populateSpinner(View view){
        if((members = ObjectStorage.getMemberList())==null) {
            try {
                members = new GetMembersAgainTask().execute().get(20, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            ObjectStorage.setMemberList(members);
        }

        List<String> names = new ArrayList<String>();
        names.add("Select User");
        for(User m: members){
            names.add(m.getName());
        }
        spinner = (Spinner)view.findViewById(R.id.edailyspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                names);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
<<<<<<< HEAD
=======
    }

    @Override
    public void onClick(View view) {
        String query = buildQuery();
        Log.e("test", query);
    }

    private String buildQuery() {

        StringBuilder sb = new StringBuilder();

        if(spinner.getSelectedItemPosition() > 0)
            sb.append("ID=").append(members.get(spinner.getSelectedItemPosition()-1).getID()).append(" AND ");
        sb.append("submitted between ").append(startDate.getText()).append(" AND ").append(endDate.getText())
                .append(" AND ");
        RadioButton temp = (RadioButton)getView().findViewById(depRG.getCheckedRadioButtonId());
        if(temp!=null){
            sb.append("dependable ").append(temp.getText());
            if(depCB.isChecked()) sb.append("= ");
            sb.append(" ").append(depNo.getText()).append(" AND ");
        }
        temp = (RadioButton)getView().findViewById(issRG.getCheckedRadioButtonId());
        if(temp!=null){
            sb.append("issues ").append(temp.getText());
            if(issCB.isChecked()) sb.append("= ");
            sb.append(" ").append(issNo.getText()).append(" AND ");
        }
        temp = (RadioButton)getView().findViewById(relRG.getCheckedRadioButtonId());
        if(temp!=null){
            sb.append("reliable ").append(temp.getText());
            if(relCB.isChecked()) sb.append("= ");
            sb.append(" ").append(relCB.getText()).append(" AND ");
        }
        if(seekBar.getProgress()>0) sb.append(" grade >= ").append(seekBar.getProgress()).append(" AND ");

        return sb.append(" 1=1 ").toString();
>>>>>>> 4dfec120dd511d589689e93e8ed2db1b12d2e143
    }
    /**
     * This class is used to get the list of members if the value in the ObjectStorage class
     * is null.
     */
    public class GetMembersAgainTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... params) {
            return MySQLQuery.getAllMembers("/DCC/getAllUsers.php");
        }


        @Override
        protected void onCancelled() {
        }
    }
}
