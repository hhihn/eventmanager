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

    private Long eventId;
    private Event selectedEvent;
    @Named
    @Inject
    private UserLoginBean userLoginBean;

    public boolean canViewParticipants(){
        User currentUser = userLoginBean.getLoggedInUser();

        if (currentUser == null || selectedEvent == null){
            return false;
        }

        if (currentUser.getRole().equals(UserRole.ADMIN)){
            return true;
        }

        User eventOrganizer = selectedEvent.getOrganizer();
        return (eventOrganizer != null && currentUser.getId().equals(eventOrganizer.getId()));
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
