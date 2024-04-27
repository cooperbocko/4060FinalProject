package edu.uga.cs.mobliefinalproject;

public class UserModel {
    public String key;
    public String email;
    public int points;


    public UserModel(String email, int points) {
        this.key = null;
        this.email = email;
        this.points = points;
    }

    public UserModel() {
        this.key = null;
        this.email = null;
        this.points = 5;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
