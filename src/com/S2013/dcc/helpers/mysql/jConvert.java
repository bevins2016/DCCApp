package com.S2013.dcc.helpers.mysql;

import android.util.Log;

import com.S2013.dcc.helpers.ActionItem;
import com.S2013.dcc.helpers.EDaily;
import com.S2013.dcc.helpers.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harmon on 8/5/13.
 */
public class jConvert {

    protected synchronized static User convertUser(JSONObject jUser){
        try{
            User user = new User();
            user.setID(Integer.parseInt(jUser.getString("ID")));
            user.setName(jUser.getString("display_name"));
            user.setEmail(jUser.getString("user_email"));
            user.setHandle(jUser.getString("user_login"));
            user.setProject(jUser.getString("project"));
            user.setProject2(jUser.getString("project2"));
            return user;
        } catch (JSONException e) {
            Log.e("dcc.MySQLQuery", e.getMessage());
        }
        return null;
    }

    protected synchronized static ActionItem convertActionItem(JSONObject jActionItem){

        ActionItem item = new ActionItem();
        try {
            item.setAid(Integer.parseInt(jActionItem.getString("id")));
            item.setDescription(jActionItem.getString("description"));
            item.setTag(jActionItem.getString("tag"));
            item.setBody(jActionItem.getString("body"));
            item.setSubject(jActionItem.getString("subject"));
            item.setDate(jActionItem.getString("date"));
            item.setTime(jActionItem.getString("time"));
            item.setStatus(Integer.parseInt(jActionItem.getString("status")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return item;
    }

    protected synchronized static EDaily converteDaily(JSONObject jEdaily){
        EDaily edaily = new EDaily();
        try {


            edaily.setFirstname(jEdaily.getString("first"));
            edaily.setLastname(jEdaily.getString("last"));
            edaily.setProj(jEdaily.getString("project"));

            edaily.setHours(jEdaily.getInt("hours"));
            edaily.setBody(jEdaily.getString("body"));

            edaily.setUsr_id(jEdaily.getInt("ID"));


            edaily.setDate(jEdaily.getString("date"));
            edaily.setSubmitted(jEdaily.getString("submitted"));
            edaily.setColor(jEdaily.getString("color"));
            edaily.setGrade(jEdaily.getInt("grade"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return edaily;
    }

}
