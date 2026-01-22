package iu.piisj.eventmanager.dto;

public class Event {

    private String name;
    private String location;
    private String date;
    private String state;

    public Event(String name, String location, String date, String state){
        this.name = name;
        this.location = location;
        this.date = date;
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }
}
