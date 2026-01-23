package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.dto.Event;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class EventBean implements Serializable {

    private List<Event> events;
    private Event newEvent;

    @PostConstruct
    public void init() {

        newEvent = new Event("", "", "", "Geplant");
    }

    public List<Event> getEvents() {
        return events;
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Offen", "Abgeschlossen");
    }

    public void addEvent() {
        events.add(newEvent);
        newEvent = new Event("", "", "", "Geplant");
    }
}