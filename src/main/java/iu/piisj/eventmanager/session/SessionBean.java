package iu.piisj.eventmanager.session;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.usermanagement.User;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@RequestScoped
public class SessionBean implements Serializable {

    @Inject
    private EventRepository eventRepository;

    private Long eventId;
    private Event selectedEvent;           // neu
    private Session newSession = new Session();

    // neu: einmal laden statt mehrfach findById in EL
    public void loadEvent() {
        if (eventId == null) {
            selectedEvent = null;
            return;
        }
        selectedEvent = eventRepository.findById(eventId);
    }

    public Event getSelectedEvent() {
        return selectedEvent;
    }

    @OrganizerOnly
    public void addSession() {
        Event e = selectedEvent != null ? selectedEvent : eventRepository.findById(eventId);

        User u = (User) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("user");

        if (u == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Nicht eingeloggt", "Bitte erneut einloggen."));
            return;
        }

        if (e == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event fehlt", "Kein Event ausgewählt."));
            return;
        }

        if (newSession.getTitle() == null || newSession.getTitle().isBlank()) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Titel fehlt", "Bitte Session-Titel angeben."));
            return;
        }

        newSession.setOrganizer(u);
        e.addSession(newSession);
        eventRepository.update(e);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Gespeichert", "Session wurde hinzugefügt."));

        newSession = new Session();
        loadEvent(); // optional: neu laden, damit Session-Liste aktualisiert ist
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Session getNewSession() { return newSession; }
    public void setNewSession(Session newSession) { this.newSession = newSession; }
}
