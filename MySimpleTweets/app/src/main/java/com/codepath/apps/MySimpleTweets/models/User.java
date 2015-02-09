package com.codepath.apps.MySimpleTweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by nehadike on 2/5/15.
 */
public class User implements Serializable{

    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private String name;
    private String screenName;
    private String profileImageUrl;
    
    public static User fromJson(JSONObject jsonObject) {
        User u = new User();
        try {
            u.uid = jsonObject.getLong("id");
            u.name = jsonObject.getString("name");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }
    
    /*public static User loggedInUserFromJson (JSONObject jsonObject) {

        User u = new User();
        try {
            u.uid = jsonObject.getLong("id");
            u.name = jsonObject.getString("name");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }*/
}
