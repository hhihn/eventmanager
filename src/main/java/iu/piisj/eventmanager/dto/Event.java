package iu.piisj.eventmanager.dto;

public class Event {

    private String name;
    private String location;
    private String date;
    private String status;

    public Event(String name, String location, String date, String status) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.status = status;
    }

    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
}