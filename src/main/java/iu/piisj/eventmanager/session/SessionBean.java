package iu.piisj.eventmanager.session;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.dto.SessionDTO;
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
    private SessionDTO newSessionDTO = new SessionDTO();

    public boolean checkAuthorization(){
        User currentUser = userLoginBean.getLoggedInUser();
        if (eventId != null){
            return sessionService.canCreateSession(currentUser, eventId);
        } else {
            return false;
        }
    }

    @OrganizerOnly
    public String createSession(){

        User currentUser = userLoginBean.getLoggedInUser();

        String validationError = validateSession();
        if (validationError != null){
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    validationError, null));
            return null;
        }

        Session session = new Session(
                newSessionDTO.getTitle(),
                newSessionDTO.getSpeaker(),
                newSessionDTO.getRoom(),
                newSessionDTO.getStartTime(),
                newSessionDTO.getEndTime()
        );

        boolean success = sessionService.createSession(session, currentUser, eventId);

        if (success) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Session erfolgreich angelegt.", null));

            // erzwinge einen reload
            // wichtig weil eventdetailbean lädt das event im eager modus, also zu beginn
            // wenn es erzeugt wird und dann nur "auf nachfrage"
            if (eventDetailBean != null){
                eventDetailBean.loadEvent();
            }

            newSessionDTO = new SessionDTO();
            // setzt eine reload der seite um
            // wichtig: seite (view) unabhängig vom bean arbeitet
            return "redirect:/event-details.xhtml?eventId=" + eventId + "&faces-redirect=true";

        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Fehler beim Erstellen der Session", null));
            return null;
        }

    }

    private String validateSession(){
        if (newSessionDTO.getTitle() == null || newSessionDTO.getTitle().trim().isEmpty()){
            return "Titel ist erforderlich";
        }

        if (newSessionDTO.getStartTime() == null || newSessionDTO.getEndTime() == null){
            return "Start- und Endzeit sind erforderlich.";
        }

        if (newSessionDTO.getStartTime().isAfter(newSessionDTO.getEndTime())){
            return "Startzeit muss vor Endzeit liegen.";
        }

        return null;
    }

    public SessionDTO getNewSessionDTO() {
        return newSessionDTO;
    }

    public void setNewSessionDTO(SessionDTO newSessionDTO) {
        this.newSessionDTO = newSessionDTO;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }
}
