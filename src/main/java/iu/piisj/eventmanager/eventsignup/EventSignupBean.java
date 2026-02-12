package iu.piisj.eventmanager.eventsignup;

import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.event.EventDetailBean;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import jakarta.annotation.PostConstruct;
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

}
