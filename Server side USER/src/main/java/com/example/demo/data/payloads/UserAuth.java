package com.example.demo.data.payloads;

import org.springframework.lang.NonNull;

import com.example.demo.data.enums.Role;

// This class is for interaction with frontend only
public class UserAuth {

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String newUsername;

    private String newPassword;

    private Role role;

    public UserAuth(){
        this.username = "";
        this.password = "";
        this.newUsername = "";
        this.newPassword = "";
        this.role = null;
    }

    public UserAuth(@NonNull String username, @NonNull String password, Role role){
        this.username = username;
        this.password = password;
        this.newUsername = "";
        this.newPassword = "";
        this.role = role;
    }

    public UserAuth(@NonNull String username, @NonNull String password, String newUsername, String newPassword, Role role){
        this.username = username;
        this.password = password;
        this.newUsername = newUsername;
        this.newPassword = newPassword;
        this.role = role;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public void setUsername(@NonNull String username){
        this.username = username;
    }

    public void setPassword(@NonNull String password){
        this.password = password;
    }

    public String getNewUsername(){
        return newUsername;
    }

    public String getNewPassword(){
        return newPassword;
    }

    public void setNewUsername(String username){
        this.newUsername = username;
    }

    public void setNewPassword(String password){
        this.newPassword = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
