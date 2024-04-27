package edu.uga.cs.mobliefinalproject;

public class RideRequestModel {
    public String key;
    public String rider;
    public String details;
    public String from;
    public String to;
    public String date;
    public boolean accepted;
    public String acceptedBy;

    @Override
    public String toString() {
        return "Rider: " + rider +
                "\nGoing from " + from + " to " + to +
                "\n at: " + date +
                "\nDetails: " + details;
    }

    public RideRequestModel(String rider, String details, String from, String to, String date, boolean accepted, String acceptedBy) {
        this.key = null;
        this.rider = rider;
        this.details = details;
        this.from = from;
        this.to = to;
        this.date = date;
        this.accepted = accepted;
        this.acceptedBy = acceptedBy;
    }

    public RideRequestModel(){
        this.key = null;
        this.rider = null;
        this.details = null;
        this.from = null;
        this.to = null;
        this.date = null;
        this.accepted = false;
        this.acceptedBy = null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRider() {
        return rider;
    }

    public void setRider(String rider) {
        this.rider = rider;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getAcceptedBy() {
        return acceptedBy;
    }

    public void setAcceptedBy(String acceptedBy) {
        this.acceptedBy = acceptedBy;
    }
}
