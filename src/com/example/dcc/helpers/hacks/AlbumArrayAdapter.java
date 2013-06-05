package com.example.dcc.helpers.hacks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.dcc.R;
import com.example.dcc.helpers.BitmapCache;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by harmonbc on 6/4/13.
 */
public class AlbumArrayAdapter  extends BaseAdapter {
    private Activity activity;
    private List<String> mBitmapList;

    public AlbumArrayAdapter(Activity activity, List<String> bitmapList) {
        super();
        this.activity = activity;
        mBitmapList = bitmapList;
    }

    @Override
    public int getCount() {
        return mBitmapList.size();
    }

    @Override
    public Object getItem(int i) {
        return mBitmapList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public static class ViewHolder
    {
        public ImageView imgViewFlag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getLayoutInflater();
        ViewHolder view;

        if(convertView == null)
        {
            view = new ViewHolder();
            convertView = inflater.inflate(R.layout.album_square, null);

            view.imgViewFlag = (ImageView)convertView.findViewById(R.id.gridImageView);
            convertView.setTag(view);
        }
        else
        {
            view = (ViewHolder) convertView.getTag();
        }

        Bitmap addThis;
        if(BitmapCache.getBitmap(mBitmapList.get(position))!=null)
             view.imgViewFlag.setImageBitmap(BitmapCache.getBitmap(mBitmapList.get(position)));
        else {
            view.imgViewFlag.setImageResource(R.drawable.deviceaccesscamera);
            new GetMediaImageTask().execute(mBitmapList.get(position));
        }

        return convertView;
    }

    public class GetMediaImageTask extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... imgUrl) {
            Bitmap image;
            try {

                Long startTime = System.currentTimeMillis();

                URL url = new URL(imgUrl[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                image = BitmapFactory.decodeStream(conn.getInputStream());
                conn.disconnect();

                BitmapCache.addBitmap(imgUrl[0],
                        createScaledBitmap(image, 200, 200, false));

                Log.e("Have Image ", "Total Time= "+(System.currentTimeMillis()-startTime));
                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result){

            notifyDataSetChanged();
        }
    }
}
