package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.eventsignup.EventSignup;
import iu.piisj.eventmanager.eventsignup.SignupStatus;
import iu.piisj.eventmanager.session.Session;
import iu.piisj.eventmanager.usermanagement.User;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne()
    @JoinColumn(name = "organizer_user_id")
    private User organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventSignup> participants = new ArrayList<>();

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

    // Getter & Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public User getOrganizer() {
        return organizer;
    }

    public void setOrganizer(User organizer) {
        this.organizer = organizer;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public List<EventSignup> getParticipants() {
        return participants;
    }

    public void setParticipants(List<EventSignup> signups) {
        this.participants = signups;
    }

    public int getSignupCount() {
        return participants.size();
    }

    public boolean isUserSignedUp(User user) {
        return participants.stream()
                .anyMatch(s -> s.getUser().getId().equals(user.getId()) &&
                        s.getStatus() == SignupStatus.REGISTERED);
    }
}
