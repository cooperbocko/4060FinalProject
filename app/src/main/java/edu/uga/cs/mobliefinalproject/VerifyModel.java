package edu.uga.cs.mobliefinalproject;

public class VerifyModel {
    public String key;
    public String refKey;
    public String driver;
    public String rider;
    public boolean driverAccepted;
    public boolean riderAccepted;
    public String type;

    public VerifyModel(String driver, String rider, boolean driverAccepted, boolean riderAccepted) {
        this.key = null;
        this.refKey = null;
        this.driver = driver;
        this.rider = rider;
        this.driverAccepted = driverAccepted;
        this.riderAccepted = riderAccepted;
        this.type = null;
    }

    public VerifyModel() {
        this.key = null;
        this.refKey = null;
        this.driver = null;
        this.rider = null;
        this.driverAccepted = false;
        this.riderAccepted = false;
        this.type = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public boolean isDriverAccepted() {
        return driverAccepted;
    }

    public void setDriverAccepted(boolean driverAccepted) {
        this.driverAccepted = driverAccepted;
    }

    public boolean isRiderAccepted() {
        return riderAccepted;
    }

    public void setRiderAccepted(boolean riderAccepted) {
        this.riderAccepted = riderAccepted;
    }

    @Override
    public String toString() {
        return "Driver " + driver + " accepted: " + driverAccepted +
                "\n Rider " + rider + " accepted: " + riderAccepted;
    }

    public String getRefKey() {
        return refKey;
    }

    public void setRefKey(String refKey) {
        this.refKey = refKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
