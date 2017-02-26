package team.model;

import team.model.enums.UserType;

public class User {
    
    private String username;
    private String password;
    private UserType userType;
    private String email;



    public User(String username, String password, UserType userType, String email) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
    }

    public User(String username, String password, UserType userType) {
        this(username, password, userType, null);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }






}
