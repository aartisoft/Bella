package com.spots.bella.models;

public class BaseUser {
    private String frist_name;
    private String last_name;
    private String email;
    private String password;
    private String phone;
    private String type;

    public BaseUser() {
    }

    public String getFrist_name() {
        return frist_name;
    }

    public void setFrist_name(String frist_name) {
        this.frist_name = frist_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
