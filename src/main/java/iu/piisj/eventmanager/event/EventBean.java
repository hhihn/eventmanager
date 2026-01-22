package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.repositories.EventRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class EventBean  implements Serializable {

    @Inject
    private EventRepository eventRepository;

    public List<iu.piisj.eventmanager.event.Event> getEvents() {
        return eventRepository.findAll();
    }

    public void addEvent() {
        Event newEvent = new Event("", "", "", "Geplant");
        eventRepository.save(newEvent);
    }
    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Ofen", "Abgeschlossen");
    }
}
