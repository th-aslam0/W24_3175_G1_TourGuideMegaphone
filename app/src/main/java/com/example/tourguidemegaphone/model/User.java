package com.example.tourguidemegaphone.model;

public class User {
    private String email;
    private String fName;
    private String lName;
    private String role;
    private String token;

    public User(String email, String token, String fName, String lName, String role) {
        this.email = email;
        this.fName = fName;
        this.lName = lName;
        this.role = role;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
