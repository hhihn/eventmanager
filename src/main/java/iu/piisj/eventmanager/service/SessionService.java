package iu.piisj.eventmanager.service;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.eventsignup.EventSignupBean;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.repository.SessionRepository;
import iu.piisj.eventmanager.session.Session;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SessionService {

    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private EventRepository eventRepository;

    public boolean canCreateSession(User user, Long eventId){

        if (user == null){
            return false;
        }

        if(user.getRole().equals(UserRole.ADMIN)){
            return true;
        }

        if(!user.getRole().equals(UserRole.ORGANISATOR)){
            return false;
        }

        Event event = eventRepository.findById(eventId);
        if (event == null || event.getOrganizer() == null){
            return false;
        }

        return user.getId().equals(event.getOrganizer().getId());
    }

    @OrganizerOnly
    public boolean createSession(Session session, User organizer, Long eventId){
        if (!canCreateSession(organizer, eventId))
        {
            return false;
        }

        Event event = eventRepository.findById(eventId);
        if (event == null) {
            return false;
        }

        session.setEvent(event);
        session.setOrganizer(organizer);

        try {
            sessionRepository.save(session);
            return true;
        } catch (Exception e) {
            return false;
        }


    }

}
