package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.dto.EventDTO;
import iu.piisj.eventmanager.repository.EventRepository;
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

    // diese annotation sorgt dafür dass nur eingeloggte user mit den rollen
    // ORGANISATOR oder ADMIN diese Methode aufrufen können
    // sonst kommt es zu einem 403 Fehler
    @OrganizerOnly
    public void saveEvent(){

        Event newEvent = this.mapDTOToEntity(this.newEventDTO);

        User u = (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        newEvent.setOrganizer(u);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Event angelegt",
                        "Es wurde ein neues Event angelegt"));

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
