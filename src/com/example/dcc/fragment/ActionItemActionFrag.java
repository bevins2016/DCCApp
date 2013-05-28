package com.example.dcc.fragment;

import android.app.Activity;
import android.app.Fragment;
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
import com.example.dcc.helpers.User;
import com.example.dcc.helpers.mysql.HttpConnection;
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
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 *
 * This class is responsible for taking in action item details and sending it to the
 * ai php script on the database.
 *
 * Created by Brandon Harmon on 5/22/13.
 */
public class ActionItemActionFrag extends Fragment implements View.OnClickListener{

    //Object defining the action item from the database
    ActionItem actionitem;
    //Textbox title of the action item body
    TextView aitext;

    EditText userInput;
    //Text area for user input
    EditText userinput;
    //Submit button
    Button aisubmit;
    //Cancel button
    Button aiCancel;
    //Current activity
    Activity activity;
    //Stores the values set inside of the action item frag
    List<NameValuePair> nameValuePairs;

    /**
     * Used to create the view for this fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.action_item_action_frag, container, false);
        super.onCreate(savedInstanceState);

        activity = getActivity();

        //Get items for this fragment
        aisubmit = (Button) view.findViewById(R.id.aisubmut);
        aiCancel = (Button) view.findViewById(R.id.aicancel);
        userInput = (EditText) view.findViewById(R.id.userinput);

        actionitem = (ActionItem)getArguments().getSerializable("actionitem");
        aitext = (TextView)view.findViewById(R.id.aitext);

        aitext.setText(actionitem.getBody());

        //Set Listeners
        aisubmit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.aisubmut:

                 nameValuePairs = new ArrayList<NameValuePair>();
                User u = ObjectStorage.getUser();
                int split = u.getName().indexOf(" ");

                nameValuePairs.add(new BasicNameValuePair("subject", actionitem.getSubject()));
                nameValuePairs.add(new BasicNameValuePair("response", (String) aitext.getText()));
                nameValuePairs.add(new BasicNameValuePair("aiid", ""+actionitem.getAid()));
                nameValuePairs.add(new BasicNameValuePair("aitag", actionitem.getTag()));

                try {
                    new Uploader().execute(null).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            //TODO: RETURN TO ACTIVITY LIST FRAGMENT
        }


    }


    /* Send eDaily to the server */
    public class Uploader extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpConnection.sendActionItem(nameValuePairs);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

			/* Update complete toast */

        }
}
}