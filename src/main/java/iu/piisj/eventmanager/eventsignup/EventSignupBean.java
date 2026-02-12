package iu.piisj.eventmanager.eventsignup;

import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class EventSignupBean {

    @Inject
    private EventSignupService signupService;

    @Inject
    private UserLoginBean userBean;

    @Inject
    private FacesContext facesContext;

    private Long eventId;
    private Event event;
    private boolean isUserSignedUp;

    @PostConstruct
    public void init() {
        if (eventId != null) {
            loadEventAndSignupStatus();
        }
    }

    private void loadEventAndSignupStatus() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser != null) {
            isUserSignedUp = signupService.isUserSignedUp(
                    currentUser.getId(),
                    eventId
            );
        }
    }

    public String signupForEvent() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser == null) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Please log in to sign up for events", null));
            return "redirect:login";
        }

        boolean success = signupService.signupUserForEvent(
                currentUser.getId(),
                eventId
        );

        if (success) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Successfully signed up for event", null));
            isUserSignedUp = true;
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to sign up. You may already be registered.", null));
        }
        return null; // Reload page
    }

    public String cancelSignup() {
        User currentUser = userBean.getLoggedInUser();
        if (currentUser == null) {
            return null;
        }

        boolean success = signupService.cancelSignup(
                currentUser.getId(),
                eventId
        );

        if (success) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                            "Successfully cancelled event signup", null));
            isUserSignedUp = false;
        } else {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Failed to cancel signup", null));
        }
        return null;
    }

    // Getters and Setters
    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isUserSignedUp() {
        return isUserSignedUp;
    }

    public void setUserSignedUp(boolean userSignedUp) {
        isUserSignedUp = userSignedUp;
    }
}