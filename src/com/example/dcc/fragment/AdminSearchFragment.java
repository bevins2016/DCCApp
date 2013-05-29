package com.example.dcc.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.dcc.R;
import com.example.dcc.helpers.EDaily;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.hacks.DCCArrayList;
import com.example.dcc.helpers.mysql.MySQLQuery;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    LinearLayout colorbox;
    TextView numvalue;
    RadioGroup depRG, relRG, issRG;
    CheckBox issCB, relCB, depCB;
    SeekBar seekBar;

    /*Executes on create view*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edaily_query,
                container, false);
        //Make a list of users names
        getFields(view);
        populateSpinner(view);
        return view;
    }

    /**
     * This gets and inits all objects that represent the layout
     * @param view
     */
    private void getFields(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

        colorbox = (LinearLayout)view.findViewById(R.id.colorvalue);
        numvalue = (TextView)view.findViewById(R.id.numvalue);

        seekBar = (SeekBar)view.findViewById(R.id.scoreSeek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                /**
                 * 0-2 red
                 * 3-5 yellow
                 * 6-13 green
                 * 14+ blue
                 */
                if(i==-1){
                    colorbox.setBackgroundColor(Color.BLACK);
                }else if(i<=2){
                    colorbox.setBackgroundColor(Color.RED);
                }else if(i<=5){
                    colorbox.setBackgroundColor(Color.YELLOW);
                }else if(i<=13){
                    colorbox.setBackgroundColor(Color.GREEN);
                }else{
                    colorbox.setBackgroundColor(Color.BLUE);
                }
                numvalue.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        search = (Button)view.findViewById(R.id.edailysearch);
        search.setOnClickListener(this);
    }

    /**
     * This validates and populates the spinner object
     * @param view
     */
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
        names.add("Select User");//Used as a default value
        for(User m: members){
            names.add(m.getName());
        }
        spinner = (Spinner)view.findViewById(R.id.edailyspinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item,
                names);

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }

    /*Ends execute on create view*/

    @Override
    public void onClick(View view) {
        String query = buildQuery();
        try {
            DCCArrayList<EDaily> edailies = new GetEdailyReports().
                    execute("/DCC/getSomeDailys.php?query="+query).get(20, TimeUnit.SECONDS);
            EDailyList detailFrag = new EDailyList();
            //Place the member to be displayed into a bundle
            Bundle bundle = new Bundle();
            bundle.putSerializable("edailies", edailies);
            detailFrag.setArguments(bundle);

            //Launch the fragment activity
            FragmentManager manager = getActivity().getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();

            ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
            transaction.replace(R.id.fragmentcontainerright, detailFrag);

            transaction.commit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks the value of each field and populates the necessary string.
     * @return HTML encoded string.
     */
    private String buildQuery() {

        StringBuilder sb = new StringBuilder();

        if(spinner.getSelectedItemPosition() > 0)
            sb.append("ID=").append(members.get(spinner.getSelectedItemPosition()-1).getID()).append(" AND ");
        sb.append("submitted BETWEEN '").append(startDate.getText()).append("' AND '").append(endDate.getText())
                .append("' AND ");
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
            sb.append(" ").append(relNo.getText()).append(" AND ");
        }
        if(seekBar.getProgress()>0) sb.append(" grade >= ").append(seekBar.getProgress()).append(" AND ");

        String str = sb.append(" 1=1 ").toString();
        try {
            Log.e("QUERY", str);
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
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
    }

    /**
     * This class is used to get the list of members if the value in the ObjectStorage class
     * is null.
     */
    public class GetEdailyReports extends AsyncTask<String, Void, DCCArrayList<EDaily>> {
        @Override
        protected DCCArrayList<EDaily> doInBackground(String... strings) {
            Log.e("this one ", strings[0]);
            return MySQLQuery.getEdailys(strings[0]);
        }
    }
}
