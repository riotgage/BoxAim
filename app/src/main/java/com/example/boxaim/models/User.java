package com.example.boxaim.models;

public class User {

    private String email;
    private String mobile;
    private String name;
    private int photos;
    private int posts;
    private String profile;
    private String user_id;

    public User(String user_id, String name, String profile, int photos, int posts, String email, String mobile) {
        this.user_id = user_id;
        this.name = name;
        this.profile = profile;
        this.photos = photos;
        this.posts = posts;
        this.email = email;
        this.mobile = mobile;
    }

    public User() {

    }

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        mobile = mobile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
