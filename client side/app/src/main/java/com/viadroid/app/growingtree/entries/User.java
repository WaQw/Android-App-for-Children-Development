package com.viadroid.app.growingtree.entries;

public class User {
    private String userName;
    private String token;
    private Baby baby;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Baby getBaby() {
        return baby;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", token='" + token + '\'' +
                ", baby=" + baby +
                '}';
    }
}
