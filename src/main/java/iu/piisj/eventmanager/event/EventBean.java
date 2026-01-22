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
        events = new ArrayList<>(List.of(
                new Event("Java EE Konferenz", "Berlin", "15.03.2026", "Geplant"),
                new Event("Cloud Workshop", "Hamburg", "22.04.2026", "Offen"),
                new Event("DevOps Summit", "Düsseldorf", "14.12.2025", "Abgeschlossen")
        ));

        newEvent = new Event("", "", "", "Geplant");
    }

    // ===== Getter =====

    public List<Event> getEvents() {
        return events;
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Offen", "Abgeschlossen");
    }

    // ===== ACTION METHOD (FEHLTE!) =====

    public void addEvent() {
        events.add(newEvent);
        newEvent = new Event("", "", "", "Geplant");
    }
}