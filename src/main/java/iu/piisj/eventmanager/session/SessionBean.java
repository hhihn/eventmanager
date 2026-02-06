package iu.piisj.eventmanager.session;

import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.repository.EventRepository;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class SessionBean implements Serializable {

    @Inject
    private EventRepository eventRepository;

    private Long eventId;        // wird aus Request-Param gesetzt (z.B. ?eventId=1)
    private Session newSession = new Session();

    public Event findEventById() {
        if (eventId == null) return null;
        return eventRepository.findById(eventId);
    }

    public void addSession() {
        Event e = findEventById();
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

        e.addSession(newSession);
        eventRepository.update(e);

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Gespeichert", "Session wurde hinzugefügt."));
        newSession = new Session();
    }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Session getNewSession() { return newSession; }
    public void setNewSession(Session newSession) { this.newSession = newSession; }
}
