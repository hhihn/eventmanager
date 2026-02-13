package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class EventDetailBean implements Serializable {

    @Inject
    private EventRepository eventRepository;

    @Inject
    private UserLoginBean userLoginBean;

    private Long eventId;
    private Event selectedEvent;

    // Prüfe, ob der aktuelle User die Teilnehmerliste sehen darf
// Admin darf alles sehen, Organisator nur sein eigenes Event
    public boolean canViewParticipants() {
        User currentUser = userLoginBean.getLoggedInUser();

        if (currentUser == null || selectedEvent == null) {
            return false;
        }

        // Admin darf ALLES sehen
        if (currentUser.getRole() == UserRole.ADMIN) {
            return true;
        }

        // Organisator darf nur seine eigenen Events sehen
        // Normale Teilnehmer sehen nichts
        return selectedEvent.getOrganizer() != null &&
                currentUser.getId().equals(selectedEvent.getOrganizer().getId());
    }

    public void loadEvent(){
        if (eventId == null){
            selectedEvent = null;
            return;
        }
        selectedEvent = eventRepository.findById(eventId);
    }

    public String getOrganizerNames(){
        loadEvent();
        User organizer = selectedEvent.getOrganizer();
        if (selectedEvent == null || organizer == null){
            return "";
        }
        return organizer.getFirstname() + " " + organizer.getName();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }
}
