package com.warunya.ricenearby.model;


public class RegisterEntity {

    private String Username;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterEntity() {
    }

    public RegisterEntity(String username, String email, String password, String confirmPassword) {
        Username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
