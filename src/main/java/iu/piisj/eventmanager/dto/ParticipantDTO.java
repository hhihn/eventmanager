package iu.piisj.eventmanager.dto;

public class ParticipantDTO {

    private String name;
    private String firstname;
    private String email;
    private String state;
    private Long eventId;

    public String getName() {
        return name;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getEmail() {
        return email;
    }

    public String getState() {
        return state;
    }

    public Long getEventId() { return eventId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setEventId(Long eventId) { this.eventId = eventId; }

    public ParticipantDTO() {}

    public ParticipantDTO(String name, String firstname, String email, String state, Long eventId){
        this.email = email;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.state = state;
        this.eventId = eventId;
    }
}
