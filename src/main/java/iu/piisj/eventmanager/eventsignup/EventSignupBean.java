package iu.piisj.eventmanager.eventsignup;

import iu.piisj.eventmanager.event.EventDetailBean;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.repository.EventSignupRepository;
import iu.piisj.eventmanager.repository.UserRepository;
import iu.piisj.eventmanager.service.EventSignupService;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Named
@ViewScoped
public class EventSignupBean implements Serializable {

    @Inject
    private EventSignupRepository signupRepository;

    @Inject
    private EventSignupService eventSignupService;

    @Inject
    private UserLoginBean userBean;

    @Inject
    private EventDetailBean eventDetailBean;  // Inject the detail bean

    @Inject
    private FacesContext facesContext;

    // Remove: private Long eventId; (not needed anymore)
    // Remove: private Event event; (use eventDetailBean.selectedEvent instead)
    private boolean isUserSignedUp;
    private List<EventSignup> eventSignups;

    @PostConstruct
    public void init() {
        if (eventDetailBean != null && eventDetailBean.getSelectedEvent() != null) {
            loadSignupStatus();
            loadSignups();
        }
    }

    private void loadSignupStatus() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser != null && eventDetailBean.getSelectedEvent() != null) {
            isUserSignedUp = eventSignupService.isUserSignedUp(
                    currentUser.getId(),
                    eventDetailBean.getSelectedEvent().getId()
            );
        }
    }

    private void loadSignups() {
        if (eventDetailBean.getSelectedEvent() != null) {
            eventSignups = signupRepository.findByEventId(
                    eventDetailBean.getSelectedEvent().getId()
            );
        }
    }

    // JSF Actions
    public String signupForEvent() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Please log in to sign up for events", null));
            return "redirect:login";
        }

        if (eventDetailBean.getSelectedEvent() == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Event not found", null));
            return null;
        }

        boolean success =  eventSignupService.signupUserForEvent(
                currentUser.getId(),
                eventDetailBean.getSelectedEvent().getId()
        );

        if (success) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Successfully signed up for event", null));
            isUserSignedUp = true;
            loadSignups();
            eventDetailBean.loadEvent();
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to sign up. You may already be registered.", null));
        }
        return null;
    }

    public String cancelSignup() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser == null) {
            return null;
        }

        if (eventDetailBean.getSelectedEvent() == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Event not found", null));
            return null;
        }

        boolean success = eventSignupService.cancelSignup(
                currentUser.getId(),
                eventDetailBean.getSelectedEvent().getId()
        );

        if (success) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Successfully cancelled event signup", null));
            isUserSignedUp = false;
            loadSignups();
            eventDetailBean.loadEvent();
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to cancel signup", null));
        }
        return null;
    }

    // Getters
    public boolean isUserSignedUp() {
        return isUserSignedUp;
    }

    public List<EventSignup> getEventSignups() {
        return eventSignups;
    }

    public int getSignupCount() {
        return eventDetailBean.getSelectedEvent() != null
                ? eventDetailBean.getSelectedEvent().getSignupCount()
                : 0;
    }
}
