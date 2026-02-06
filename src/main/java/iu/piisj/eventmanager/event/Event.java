package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.participant.Participant;
import iu.piisj.eventmanager.session.Session;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();

    // NEU: Sessions
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    private String name;
    private String location;
    private String date;
    private String state;

    protected Event(){}

    public Event(String name, String location, String date, String state){
        this.name = name;
        this.location = location;
        this.date = date;
        this.state = state;
    }

    // NEU: convenience method (Session korrekt verknüpfen)
    public void addSession(Session s) {
        s.setEvent(this);
        sessions.add(s);
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public List<Participant> getParticipants() { return this.participants; }
}
