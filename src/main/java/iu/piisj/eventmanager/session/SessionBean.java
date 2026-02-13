package iu.piisj.eventmanager.session;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.event.EventDetailBean;
import iu.piisj.eventmanager.service.SessionService;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
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
    private SessionService sessionService;

    @Inject
    private UserLoginBean userLoginBean;

    @Inject
    private EventDetailBean eventDetailBean;

    @Inject
    private FacesContext facesContext;

    private Long eventId;
    private Session newSession = new Session();

    public boolean checkAuthorization() {
        User currentUser = userLoginBean.getLoggedInUser();
        if (eventId != null) {
            return sessionService.canCreateSession(currentUser, eventId);
        } else {
            return false;
        }
    }

    @OrganizerOnly
    public String createSession() {
        User currentUser = userLoginBean.getLoggedInUser();
        boolean isAuthorized = checkAuthorization();
        if (eventId == null || currentUser == null || !isAuthorized) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Es ist ein Fehler aufgetreten", null));
            return "redirect:index";
        }

        // Validierung
        if (newSession.getTitle() == null || newSession.getTitle().trim().isEmpty()) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Titel ist erforderlich", null));
            return null;
        }

        if (newSession.getStartTime() == null || newSession.getEndTime() == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Start- und Endzeit sind erforderlich", null));
            return null;
        }

        if (newSession.getStartTime().isAfter(newSession.getEndTime())) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Startzeit muss vor Endzeit liegen", null));
            return null;
        }

        boolean success = sessionService.createSession(newSession, currentUser, eventId);

        if (success) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Session erfolgreich erstellt", null));
            // Reload Event um Sessions zu aktualisieren
            if (eventDetailBean != null) {
                eventDetailBean.loadEvent();
            }
            newSession = new Session();
            return "redirect:/event-details.xhtml?eventId=" + eventId + "&faces-redirect=true";
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Sie haben keine Berechtigung oder es ist ein Fehler aufgetreten", null));
            return null;
        }
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Session getNewSession() {
        return newSession;
    }

    public void setNewSession(Session newSession) {
        this.newSession = newSession;
    }

}