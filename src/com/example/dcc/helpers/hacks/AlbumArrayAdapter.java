package com.example.dcc.helpers.hacks;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.dcc.R;
import com.example.dcc.helpers.BitmapCache;
import com.example.dcc.helpers.ImageWithBool;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import static android.graphics.Bitmap.createScaledBitmap;

/**
 * This adapter is used to manage the layout for the Album grid view
 * Created by harmonbc on 6/4/13.
 */
public class AlbumArrayAdapter  extends BaseAdapter {
    private Activity activity;
    private List<ImageWithBool> mBitmapList;
    LayoutInflater inflater;

    /**
     *
     * @param activity
     * @param bitmapList
     */
    public AlbumArrayAdapter(Activity activity, List<ImageWithBool> bitmapList) {
        super();
        this.activity = activity;
        mBitmapList = bitmapList;
        inflater = (LayoutInflater) activity.getLayoutInflater();
    }

    /**
     * Returns the number of items to display
     * @return
     */
    @Override
    public int getCount() {
        return mBitmapList.size();
    }

    /**
     * Returns the ImageItemWithBool
     * @param i
     * @return
     */
    @Override
    public Object getItem(int i) {
       return mBitmapList.get(i);
    }

    /**
     * Obligatory implementation
     * @param i
     * @return
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Class that holds the image view, used in case we want to implement additional
     * views like adding a textview under the image.
     */
    public static class ViewHolder
    {
        public ImageView imgViewFlag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
        if(mBitmapList.get(position).bool)
            view.imgViewFlag.setImageBitmap(mBitmapList.get(position).image);
        else {
            view.imgViewFlag.setImageResource(R.drawable.deviceaccesscamera);
            int errorCount = 0;
            boolean success = false;
            do{
                try{
                    new GetMediaImageTask().executeOnExecutor(
                            AsyncTask.SERIAL_EXECUTOR, mBitmapList.get(position));
                    success =true;
                }catch(RejectedExecutionException e){
                    errorCount ++;
                }
            }while(!success && errorCount<5);

        }

        return convertView;
    }

    public class GetMediaImageTask extends AsyncTask<ImageWithBool, Void, ImageWithBool>{

        @Override
        protected ImageWithBool doInBackground(ImageWithBool... img) {
            Bitmap image;
            try {

                Long startTime = System.currentTimeMillis();

                URL url = new URL(img[0].url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                image = BitmapFactory.decodeStream(conn.getInputStream());
                conn.disconnect();

                BitmapCache.addBitmap(img[0].url,
                        createScaledBitmap(image, 200, 200, false));
                img[0].image = BitmapCache.getBitmap(img[0].url);
                img[0].bool = true;

                Log.e("Have Image ", "Total Time= "+(System.currentTimeMillis()-startTime));
                return img[0];
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ImageWithBool result){
            notifyDataSetChanged();
        }
    }
}
