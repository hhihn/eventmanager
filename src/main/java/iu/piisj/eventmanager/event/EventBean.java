package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.dto.EventDTO;
import iu.piisj.eventmanager.repository.EventRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class EventBean {

    @Inject
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Ofen", "Abgeschlossen");
    }
}
