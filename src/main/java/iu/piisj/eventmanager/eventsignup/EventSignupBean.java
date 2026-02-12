package iu.piisj.eventmanager.eventsignup;

import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.repository.EventSignupRepository;
import iu.piisj.eventmanager.service.EventSignupService;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.event.EventDetailBean;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EventSignupBean  implements Serializable {

    @Inject
    private EventDetailBean eventDetailBean;

    @Inject
    private UserLoginBean userLoginBean;

    @Inject
    private EventSignupService eventSignupService;

    @Inject
    private EventSignupRepository eventSignupRepository;

    @Inject
    private FacesContext facesContext;

    private boolean isUserSignedUp;
    private List<EventSignup> eventSignups;

    // wird nach dem constructor aufgerufen
    @PostConstruct
    public void init(){
        if (eventDetailBean != null && eventDetailBean.getSelectedEvent() != null){
            loadSignupStatus();
            loadSignups();
        }
    }

    private void loadSignups() {
        if (eventDetailBean.getSelectedEvent() != null){
            // hole die id aus dem event detail bean
            // und lade alle signups zu dieser id
            eventSignups = eventSignupRepository.findByEventId(
                    eventDetailBean.getSelectedEvent().getId()
            );
        }
    }

    private void loadSignupStatus(){
        // lade den eingeloggten user
        User currentUser = userLoginBean.getLoggedInUser();
        if (currentUser != null && eventDetailBean.getSelectedEvent() != null){
            // schaue nach, ob dieser user zu dem aktuellen event angemeldet ist
            // sucht nach einer vorhandenen id kombination
            isUserSignedUp = eventSignupService.isUserSignedUp(
                    currentUser.getId(),
                    eventDetailBean.getSelectedEvent().getId()
            );
        }
    }

    public String signupUserForEvent(){
        User currentUser = userLoginBean.getLoggedInUser();
        if (currentUser == null){
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Bitte anmelden.", null));
            return "redirect:login";
        }

        Event currentEvent = eventDetailBean.getSelectedEvent();
        if (currentEvent == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Event nicht gefunden.", null));
            return null;
        }
        // implementierung der geschäftslogik im service
        // hier im bean ist nur die kommunikation implementiert
        boolean success = eventSignupService.signupUserForEvent(
                currentUser.getId(),
                currentEvent.getId()
        );

        if (success){
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Sie haben sich erfolgreich für dieses Event registriert.", null));
            isUserSignedUp = true;
            loadSignups();
            eventDetailBean.loadEvent();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Registrierung fehlgeschlagen.", null));
        }
        return null;
    }

    public void cancelSignup(){
        User currentUser = userLoginBean.getLoggedInUser();
        if (currentUser == null){
            return;
        }

        Event currentEvent = eventDetailBean.getSelectedEvent();
        if (currentEvent == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event nicht gefunden", null));
            return;
        }

        boolean success = eventSignupService.cancelSignup(
                currentUser.getId(),
                currentEvent.getId()
        );

        if (success) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Erfolgreich vom Event abgemeldet", null));
            isUserSignedUp = false;
            loadSignups();
            eventDetailBean.loadEvent();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Abmelden war nicht erfolgreich.", null));
        }
    }

    public boolean isUserSignedUp() {
        return isUserSignedUp;
    }

    public void setUserSignedUp(boolean userSignedUp) {
        isUserSignedUp = userSignedUp;
    }

    public List<EventSignup> getEventSignups() {
        return eventSignups;
    }

    public void setEventSignups(List<EventSignup> eventSignups) {
        this.eventSignups = eventSignups;
    }
}
