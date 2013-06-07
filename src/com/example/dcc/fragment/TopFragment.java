package com.example.dcc.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.*;
import com.example.dcc.R;
import com.example.dcc.helpers.BitmapCache;
import com.example.dcc.helpers.ObjectStorage;
import com.example.dcc.helpers.OnButtonSelectedListener;
import com.example.dcc.helpers.User;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Just Kidding, this is actually the top fragment
 */
public class TopFragment extends Fragment implements View.OnClickListener{

    //Toggles the menu visibility
    private Button menuVisibility;
    //Menu Width
    private ImageView userIcon;
    private OnButtonSelectedListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_fragment,
                container, false);

        //Get needed fields
        userIcon = (ImageView) view.findViewById(R.id.usericon);
        TextView textView = (TextView) view.findViewById(R.id.userdata);
        menuVisibility = (Button)view.findViewById(R.id.menu_button);

        //Set listener
        menuVisibility.setOnClickListener(this);

        //Set unique data
        //userIcon.setImageBitmap(ObjectStorage.getUser().getImage());

        String uri = "/DCC/getUserGravitar.php?email=" + ObjectStorage.getUser().getEmail();
        if(BitmapCache.getBitmap(uri)!=null) userIcon.setImageBitmap( BitmapCache.getBitmap(uri));
        else new GetImageTask().execute(ObjectStorage.getUser());

        textView.setText(ObjectStorage.getUser().getName());

        return view;
    }

    /**Take action on the button press*/
    @Override
    public void onClick(View view) {
            listener.onMenuButtonSelected(view.getId());
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

    public class GetImageTask extends AsyncTask<User, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(User... user) {
            Bitmap image;
            try{
                String uri = "/DCC/getUserGravitar.php?email=" + user[0].getEmail();

                URL url = new URL(user[0].getImageURL());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                image = BitmapFactory.decodeStream(conn.getInputStream());
                conn.disconnect();

                BitmapCache.addBitmap(uri, image);
                return image;
            }catch(Exception e){
                return null;
            }
        }


        @Override
        protected void onPostExecute(Bitmap result){
            if((result!=null)&&!isCancelled()){
                userIcon.setImageBitmap(result);
            }
        }
    }
}
