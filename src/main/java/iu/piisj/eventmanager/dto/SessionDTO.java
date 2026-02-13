package iu.piisj.eventmanager.dto;

import iu.piisj.eventmanager.session.Session;

import java.time.LocalDateTime;

public class SessionDTO {

    private String title;
    private String speaker;
    private String room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public SessionDTO() {}

    public SessionDTO(String title, String speaker, String room, LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.speaker = speaker;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
