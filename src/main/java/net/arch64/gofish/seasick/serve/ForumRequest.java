package net.arch64.gofish.seasick.serve;

import net.arch64.gofish.seasick.forums.Forum;

import java.util.ArrayList;

public class ForumRequest {
    private int userId;
    private String countryCode;
    private String region;
    private String locale;
    private Forum post;
    private ArrayList<Forum> list;

    public ForumRequest() {

    }

    public ForumRequest(Forum post) {
        this.post = post;
    }

    public ForumRequest(ArrayList<Forum> list) {
        this.list = list;
    }

    public ForumRequest(int userId, String countryCode, String region, String locale) {
        this.userId = userId;
        this.countryCode = countryCode;
        this.region = region;
        this.locale = locale;
    }

    public void setUser(int userId) { this.userId = userId; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public void setRegion(String region) { this.region = region; }
    public void setLocale(String locale) { this.locale = locale; }
    public void setPost(Forum post) { this.post = post; }
    public void setList(ArrayList<Forum> list) { this.list = list; }

    public int getUser() { return userId; }
    public String getCountryCode() { return countryCode; }
    public String getRegion() { return region; }
    public String getLocale() { return locale; }
    public Forum getPost() { return post; }
    public ArrayList<Forum> getList() { return list; }
}

