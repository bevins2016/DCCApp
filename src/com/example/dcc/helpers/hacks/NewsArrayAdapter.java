package com.example.dcc.helpers.hacks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dcc.R;
import com.example.dcc.helpers.BitmapCache;
import com.example.dcc.helpers.News;
import com.example.dcc.helpers.User;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 *
 * Created by harmonbc on 5/29/13.
 */
public class NewsArrayAdapter extends ArrayAdapter<News> {
    private final Context context;
    private final List<News> newsList;
    private ImageView icon;


    public NewsArrayAdapter(Context context, List<News> news) {
        super(context, R.layout.news_item);
        this.context = context;
        this.newsList = news;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.news_item, parent, false);
        TextView title = (TextView)rowView.findViewById(R.id.newsItemTitle);
        TextView date = (TextView)rowView.findViewById(R.id.newsdate);
        TextView name = (TextView)rowView.findViewById(R.id.newsname);
        TextView body = (TextView)rowView.findViewById(R.id.newsbodyprev);
        icon = (ImageView)rowView.findViewById(R.id.newsimageView);

        News news = newsList.get(position);

        title.setText(Html.fromHtml(news.getTitle()));
        date.setText(news.getPubdate());
        name.setText(news.getPublisher().getName());
        String text = news.getText();
        if(text.length() > 100) text = text.substring(0,100);
        body.setText(text);

        String uri = "/DCC/getUserGravitar.php?email=" + news.getPublisher().getEmail();
        if(BitmapCache.getBitmap(uri)!=null) icon.setImageBitmap( BitmapCache.getBitmap(uri));
        else {
            new GetImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, news.getPublisher());
        }

        return rowView;
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
                icon.setImageBitmap(result);
                notifyDataSetChanged();

            }else{
                return;
            }
        }
    }
}
