package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.dcc.R;
import com.example.dcc.helpers.ActionItem;
import com.example.dcc.helpers.ObjectStorage;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Harmon on 5/22/13.
 */
public class ActionItemActionFrag extends Fragment implements View.OnClickListener{

    ActionItem actionitem;
    TextView aitext;
    EditText userinput;
    Button aisubmit;
    Button aicancel;
    Activity activity;
    String first = "Sam";
    String last = "Bevins";
    String name = "";
    private String url = "http://www.virtualdiscoverycenter.net/wp-content/plugins/buddypress/bp-themes/bp-default/ai-submit.php";//http://www.facebook.com/l.php?u=http%3A%2F%2Fwww.virtualdiscoverycenter.net%2Fwp-content%2Fplugins%2Fbuddypress%2Fbp-themes%2Fbp-default%2FeDaily.php&h=3AQH7TTNw

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.action_item_action_frag, container, false);
        super.onCreate(savedInstanceState);

        activity = getActivity();

        aisubmit = (Button) view.findViewById(R.id.aisubmut);

        actionitem = (ActionItem)this.getArguments().getSerializable("actionitem");

        aitext = (TextView)view.findViewById(R.id.aitext);
        aitext.setText(actionitem.getBody());

        aisubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        /*
        MemberDetailFragment detailFrag = new MemberDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("actionitem", actionitem);
        detailFrag.setArguments(bundle);

        FragmentManager manager = getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment old = ObjectStorage.getFragment(R.id.fragmentcontainerright);
        ObjectStorage.setFragment(R.id.fragmentcontainerright, detailFrag);
        transaction.replace(R.id.fragmentcontainerright, detailFrag);

        transaction.commit();*/

        switch(view.getId()){
            case R.id.aisubmut:
                File f = new File(Environment.getExternalStorageDirectory() + "/enotebook/InternalStorage.txt");

                String studentID = "";
                try {
                    BufferedReader br = new BufferedReader(new FileReader(new File(
                            "sdcard/eNotebook/InternalStorage.txt")));
                    br.readLine(); // Skip Title
                    studentID = br.readLine(); // Get Student ID#
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uploader uploader = new Uploader();
                uploader.execute(url, "");
                break;
        }


    }


    /* Send eDaily to the server */
    public class Uploader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

			/* Send to server */
            try {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//				nameValuePairs.add(new BasicNameValuePair("id", params[1]));
//				nameValuePairs.add(new BasicNameValuePair("type", "edaily"));
//				nameValuePairs.add(new BasicNameValuePair("edaily",
//						getEditText(todayTF)));


                nameValuePairs.add(new BasicNameValuePair("uid", "" + ObjectStorage.getUser().getID()));
                nameValuePairs.add(new BasicNameValuePair("first", first));
                nameValuePairs.add(new BasicNameValuePair("uemail", "bevins2012@hotmail.com"));
                nameValuePairs.add(new BasicNameValuePair("last", last));
                nameValuePairs.add(new BasicNameValuePair("subject", "test subject"));
                nameValuePairs.add(new BasicNameValuePair("response", "" + aitext.getText()));
                nameValuePairs.add(new BasicNameValuePair("aiid", "" + actionitem.getAid()));
                nameValuePairs.add(new BasicNameValuePair("aitag", "test"));

                //  nameValuePairs.add(new BasicNameValuePair("reportdate", data.getDate()));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();
                System.out.println(nameValuePairs);
            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }

			/* Write to local file */
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("MMddyy", Locale.US);
                BufferedReader br = new BufferedReader(new FileReader(new File(
                        "sdcard/eNotebook/InternalStorage.txt")));
                br.readLine(); // Skip Title
                String studentID = br.readLine(); // Get Student ID#
                br.close();
                String filename = "/enotebook/edailys/" + studentID + "_" + sdf.format(new Date()) + "_edaily.txt";
                FileWriter internal = new FileWriter(Environment.getExternalStorageDirectory()
                        + filename);
                internal.append("");//getEditText(todayTF)
                internal.flush();
                internal.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

			/* Update complete toast */
            Toast.makeText(activity, "Report Sent!",
                    Toast.LENGTH_SHORT).show();
        }
}
}