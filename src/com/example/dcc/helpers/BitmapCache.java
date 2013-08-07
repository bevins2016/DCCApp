package com.example.dcc.helpers;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Class that utilizes 1/8 of the available memory as a image cache
 * Created by harmonbc on 5/30/13.
 */
public class BitmapCache {

    //Store the cache by the image url
    private static LruCache<String, Bitmap> mMemoryCache;
    //Max Memory Size
    private final static int maxMemory = (int)(Runtime.getRuntime().maxMemory()/1024);
    //Max Cache Size
    private final static int cacheSize = maxMemory/8;

    /*
     *Get the bitmap or return null if it doesn't exist
     */
    public static Bitmap getBitmap(String key){
        if(mMemoryCache == null) buildCache();
        return mMemoryCache.get(key);
    }

    /**
     * Add a image to the cache if it doesn't already exist
     * @param key
     * @param bitmap
     */
    public static void addBitmap(String key, Bitmap bitmap){
        if(mMemoryCache == null) buildCache();
        if(getBitmap(key)==null){
            mMemoryCache.put(key, bitmap);
        }
    }

    /**
     * Initialize the cache
     */
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
