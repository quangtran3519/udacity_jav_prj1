package com.udacity.jwdnd.course1.cloudstorage.model;

public class User {

    private Integer userId;
    private String userName;
    private String lastName;
    private String firstName;

    private String salt;
    private String password;

    public User(Integer userId, String userName, String lastName, String firstName, String salt, String password) {
        this.userId = userId;
        this.userName = userName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.salt = salt;
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
