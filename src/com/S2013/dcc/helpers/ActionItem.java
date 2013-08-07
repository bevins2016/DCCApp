package com.S2013.dcc.helpers;

import java.io.Serializable;

/**
 * Class that represents an action item fetched from the VDC database.
 * Filled with getters and setters for each item in the structure.
 * Created by Harmon on 5/22/13.
 */
public class ActionItem implements Serializable {

    /**Action ID*/
    private int aid;
    /**AI descriotion*/
    private String description;
    /**AI tag*/
    private String tag;
    /**AI Subject*/
    private String subject;
    /**AI instructions*/
    private String body;
    /**Date posted*/
    private String date;
    /**Time posted*/
    private String time;
    /**Status (1 if complete, 0 else)*/
    private int status;

    /**
     * Constructor
     */
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

    /**Return Action Item ID*/
    public int getAid() {
        return aid;
    }

    /**Set Action Item ID*/
    public void setAid(int aid) {
        this.aid = aid;
    }
    /**Return Action Item Description*/
    public String getDescription() {
        return description;
    }
    /**Set Action Item Description*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**Set Action Item Tag*/
    public String getTag() {
        return tag;
    }
    /**Return Action Item Tag*/
    public void setTag(String tag) {
        this.tag = tag;
    }
    /**Return Action Item Subject*/
    public String getSubject() {
        return subject;
    }
    /**Set Action Item Subject*/
    public void setSubject(String subject) {
        this.subject = subject;
    }
    /**Return Action Item Body*/
    public String getBody() {
        return body;
    }
    /**Set Action Item Body*/
    public void setBody(String body) {
        this.body = body;
    }
    /**Return Action Item Due Date*/
    public String getDate() {
        return date;
    }
    /**Set Action Item Due Date*/
    public void setDate(String date) {
        this.date = date;
    }
    /**Return Action Item Time*/
    public String getTime() {
        return time;
    }
    /**Set Action Item Time*/
    public void setTime(String time) {
        this.time = time;
    }
    /**Return Action Item Status*/
    public int getStatus() {
        return status;
    }

    /**Set Action Item Status*/
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
