package iu.piisj.eventmanager.event;
import iu.piisj.eventmanager.dto.EventDTO;
import iu.piisj.eventmanager.repository.EventRepository;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EventBean implements Serializable {

    @Inject
    private EventRepository eventRepository;
    private EventDTO newEvent = new EventDTO();

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Offen", "Abgeschlossen");
    }

    public void saveEvent() {
        Event eventEntity = mapToEntity(newEvent);
        eventRepository.save(eventEntity);
        FacesMessage message = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Erfolg! Die Veranstaltung wurde erfolgreich angelegt.",
                eventEntity.toString()
        );

        FacesContext.getCurrentInstance().addMessage(null, message);
        // Formular zurücksetzen
        newEvent = new EventDTO();
    }

    private Event mapToEntity(EventDTO dto) {
        return new Event(
                dto.getName(),
                dto.getLocation(),
                dto.getDate(),
                dto.getState()
        );
    }

    public EventDTO getNewEvent() {
        return newEvent;
    }
}