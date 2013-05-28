package com.example.dcc.helpers;

import java.io.Serializable;

/**
 * Class that represents an action item fetched from the VDC database.
 * Filled with getters and setters for each item in the structure.
 * Created by Harmon on 5/22/13.
 */
public class ActionItem implements Serializable {

    //Action ID
    private int aid;
    //AI descriotion
    private String description;
    //AI tag
    private String tag;
    //AI Subject
    private String subject;
    //AI instructions
    private String body;
    //Date posted
    private String date;
    //Time posted
    private String time;
    //Status (1 if complete, 0 else)
    private int status;

    public ActionItem(){
        this.aid=0;
        this.description=null;
        this.tag=null;
        this.subject=null;
        this.body=null;
        this.date=null;
        this.time=null;
        this.status=0;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(subject).append("</b><br/>").append(date).append("<br/>").append(description);
        return sb.toString();
    }

}
