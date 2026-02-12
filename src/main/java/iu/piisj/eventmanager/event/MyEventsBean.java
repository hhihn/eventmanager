package iu.piisj.eventmanager.event;

import iu.piisj.eventmanager.eventsignup.EventSignup;
import iu.piisj.eventmanager.repository.EventSignupRepository;
import iu.piisj.eventmanager.service.EventSignupService;
import iu.piisj.eventmanager.usermanagement.User;
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
public class MyEventsBean implements Serializable {

    @Inject
    private EventSignupRepository signupRepository;

    @Inject
    private EventSignupService eventSignupService;

    @Inject
    private UserLoginBean userLoginBean;

    @Inject
    private FacesContext facesContext;

    private List<EventSignup> mySignups;

    @PostConstruct
    public void init() {
        loadMyEvents();
    }

    private void loadMyEvents() {
        User currentUser = userLoginBean.getLoggedInUser();
        if (currentUser != null) {
            mySignups = signupRepository.findByUserId(currentUser.getId());
        }
    }

    public String cancelSignup(Long signupId) {
        // Finde das Signup und extrahiere userId und eventId
        for (EventSignup signup : mySignups) {
            if (signup.getId().equals(signupId)) {
                boolean success = eventSignupService.cancelSignup(
                        signup.getUser().getId(),
                        signup.getEvent().getId()
                );

                if (success) {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_INFO,
                                    "Event-Anmeldung erfolgreich storniert", null));
                    loadMyEvents();  // Neuladen der Liste
                } else {
                    facesContext.addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                    "Fehler beim Stornieren der Anmeldung", null));
                }
                break;
            }
        }
        return null;
    }

    // Getters
    public List<EventSignup> getMySignups() {
        return mySignups;
    }

    public int getSignupCount() {
        return mySignups != null ? mySignups.size() : 0;
    }
}