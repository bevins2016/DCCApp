package com.example.dcc.helpers;

import java.io.Serializable;

/**
 * Created by Harmon on 5/16/13.
 */
public class Member implements Serializable{

    private String imageURL;
    private String name;
    private String memURL;
    private String handle;

    public Member(){
    }

    public void setName(String name){
        this.name = name;
    }

    public void setImageURL(String imageURL){
        this.imageURL = imageURL;
    }

    public void setMemURL(String memURL){
        this.memURL = memURL;
    }

    public void setHandle(String handle){
        this.handle = handle;
    }

    public String getHandle(){
        return handle;
    }

    public String getName(){
        return name;
    }

    public String getImageURL(){
        return imageURL;
    }

    public String getMemURL(){
        return memURL;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        return sb.toString();
    }
}
