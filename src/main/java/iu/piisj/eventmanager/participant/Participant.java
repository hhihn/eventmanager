package iu.piisj.eventmanager.participant;
import iu.piisj.eventmanager.event.Event;
import jakarta.persistence.*;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String state;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    protected Participant() {
        // JPA
    }

    public Participant(String firstname, String lastname,
                       String email, String state, Event event) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.state = state;
        this.event = event;
    }

    // Getter & Setter
    public Long getId() { return id; }
    public String getFirstname() { return firstname; }
    public String getLastname() { return lastname; }
    public String getEmail() { return email; }
    public String getState() { return state; }
    public Event getEvent() { return event; }

    public void setEvent(Event event) { this.event = event; }
}