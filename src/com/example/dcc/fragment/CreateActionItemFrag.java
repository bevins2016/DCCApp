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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.dcc.R;
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
 * Created by Sam on 5/24/13.
 */
public class CreateActionItemFrag extends Fragment implements View.OnClickListener {
    Activity activity;
    String first = "";
    String last = "";
    String name = "";
    Button submit;
    EditText tag;
    EditText date;
    EditText time;
    EditText title;
    EditText content;
    CheckBox glCheck;
    CheckBox dglCheck;
    CheckBox mentorCheck;
    CheckBox plCheck;
    CheckBox emCheck;
    CheckBox fifteenCheck;
    CheckBox tenCheck;
    CheckBox learningCheck;


    private String url = "http://www.virtualdiscoverycenter.net/wp-content/plugins/buddypress/bp-themes/bp-default/ai-post.php";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.createactionitem, container, false);
        super.onCreate(savedInstanceState);

        activity = getActivity();

        tag = (EditText) view.findViewById(R.id.edittag);
        //date = (EditText) view.findViewById(R.id.editdate);
        time = (EditText) view.findViewById(R.id.edittime);
        title = (EditText) view.findViewById(R.id.edittitle);
        content = (EditText) view.findViewById(R.id.editbody);
        submit = (Button) view.findViewById(R.id.createai);

        glCheck = (CheckBox) view.findViewById(R.id.groupleadcheckBox);
        dglCheck = (CheckBox) view.findViewById(R.id.deputyleadcheckBox);
        mentorCheck = (CheckBox) view.findViewById(R.id.mentorcheckBox);
        plCheck = (CheckBox) view.findViewById(R.id.projectcheckBox);
        emCheck = (CheckBox) view.findViewById(R.id.eventcheckBox);
        fifteenCheck = (CheckBox) view.findViewById(R.id.fifteencheckBox);
        tenCheck = (CheckBox) view.findViewById(R.id.tencheckBox);
        learningCheck = (CheckBox) view.findViewById(R.id.learningcheckBox);

        submit.setOnClickListener(this);

        boolean helper = false;

        name = ObjectStorage.getUser().getName();

        for(int i = 0; i < name.length(); i++){

            if(name.charAt(i) == ' '){
                helper = true;
                continue;
            }
            if (helper){
                last += name.charAt(i);
            } else{
                first += name.charAt(i);
            }
        }
        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.createai:
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

                nameValuePairs.add(new BasicNameValuePair("tag", "" + tag.getText()));
                nameValuePairs.add(new BasicNameValuePair("ai-date", "" + date.getText()));
                nameValuePairs.add(new BasicNameValuePair("time", "" + time.getText()));
                nameValuePairs.add(new BasicNameValuePair("title", "" + title.getText()));
                nameValuePairs.add(new BasicNameValuePair("content", "" + content.getText()));

                //these will check if each checkbox is sent and add the appropriate tags
                if(glCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("gl", "gl"));
                }
                if(dglCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("dl", "dl"));
                }
                if(mentorCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("mm", "mm"));
                }
                if(plCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("pl", "pl"));
                }
                if(emCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("em", "em"));
                }
                if(fifteenCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("15", "15"));
                }
                if(tenCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("10", "10"));
                }
                if(learningCheck.isChecked()){
                    nameValuePairs.add(new BasicNameValuePair("lr", "lr"));
                }

//                nameValuePairs.add(new BasicNameValuePair("tag", "" + "Justice League"));
//                nameValuePairs.add(new BasicNameValuePair("ai-date", "05/24"));
//                nameValuePairs.add(new BasicNameValuePair("time", "2:14pm"));
//                nameValuePairs.add(new BasicNameValuePair("title", "Superman will LaserEyes everyone"));
//                nameValuePairs.add(new BasicNameValuePair("content", "Superman flies a mile into the air, shoots his lasers and kill his enemies. End of movie."));
//                nameValuePairs.add(new BasicNameValuePair("15", "15"));

//                HttpConnection.postResponse(url, nameValuePairs, false);
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(params[0]);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                response.getEntity();

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
