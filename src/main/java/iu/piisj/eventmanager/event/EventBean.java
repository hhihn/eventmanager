package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.dto.EventDTO;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.usermanagement.OrganizerOnly;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class EventBean {

    @Inject
    private EventRepository eventRepository;
    private EventDTO newEventDTO = new EventDTO();

    @OrganizerOnly
    public void saveEvent(){

        Event newEvent = this.mapDTOToEntity(this.newEventDTO);
        this.eventRepository.save(newEvent);

    }

    private Event mapDTOToEntity(EventDTO dto){
        return new Event(dto.getName(), dto.getLocation(), dto.getDate(), dto.getState());
    }

    public List<Event> getAllEvents() {
        return this.eventRepository.findAll();
    }

    public List<String> getAvailableStatuses() {
        return List.of("Geplant", "Offen", "Abgeschlossen");
    }

    public EventDTO getNewEventDTO() {
        return newEventDTO;
    }

    public void setNewEventDTO(EventDTO newEventDTO) {
        this.newEventDTO = newEventDTO;
    }
}
