package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.dto.Event;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class EventBean {

    public List<Event> getEvents() {

        return List.of(
                new Event("Java EE Konferenz", "Berlin", "15.03.2026", "Geplant"),
                new Event("Cloud Workshop", "Hamburg", "22.04.2026", "Offent"),
                new Event("DevOps Summit", "Düsseldorf", "14.12.2025", "Abgeschlossen")
        );

    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Ofen", "Abgeschlossen");
    }
}
