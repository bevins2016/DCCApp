package com.example.dcc.helpers;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by harmonbc on 5/30/13.
 */
public class BitmapCache {

    private static LruCache<String, Bitmap> mMemoryCache;
    private final static int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
    private final static int cacheSize = maxMemory/8;

    public static Bitmap getBitmap(String key){
        if(mMemoryCache == null) buildCache();
        return mMemoryCache.get(key);
    }

    public static void addBitmap(String key, Bitmap bitmap){
        if(mMemoryCache == null) buildCache();
        if(getBitmap(key)==null){
            mMemoryCache.put(key, bitmap);
        }
    }

    private static void buildCache() {
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize){
          @Override
        protected int sizeOf(String key, Bitmap bitmap){
              //Measure cache in size and not count
              return bitmap.getByteCount() / 1024;
          }
        };

    }
}
