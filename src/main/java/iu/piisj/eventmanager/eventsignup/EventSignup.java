package iu.piisj.eventmanager.eventsignup;

import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.usermanagement.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_signups", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "event_id"})
})
public class EventSignup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private LocalDateTime signupDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignupStatus status = SignupStatus.REGISTERED;

    protected EventSignup() {}

    public EventSignup(User user, Event event) {
        this.user = user;
        this.event = event;
        this.signupDate = LocalDateTime.now();
        this.status = SignupStatus.REGISTERED;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getSignupDate() {
        return signupDate;
    }

    public void setSignupDate(LocalDateTime signupDate) {
        this.signupDate = signupDate;
    }

    public SignupStatus getStatus() {
        return status;
    }

    public void setStatus(SignupStatus status) {
        this.status = status;
    }
}