package com.viadroid.app.growingtree;

import android.app.Application;
import android.content.Context;

import com.viadroid.app.growingtree.entries.Baby;
import com.viadroid.app.growingtree.entries.User;
import com.viadroid.app.growingtree.util.Sp;

public class App extends Application {

    private static App sApp;
    private Sp mSp;
    private User mUser;
    private Baby mBaby;

    public static App getApp() {
        return sApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        mSp = new Sp.Builder(this).setMode(Context.MODE_PRIVATE).build();
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public String getUserName() {
        if (mUser != null) {
            return mUser.getUserName();
        }
        return "";
    }

    public String getToken() {
        if (mUser != null) {
            return mUser.getToken();
        }
        return "";
    }

    public Baby getBaby() {
        return mBaby;
    }

    public void setBaby(Baby baby) {
        mBaby = baby;
    }
}