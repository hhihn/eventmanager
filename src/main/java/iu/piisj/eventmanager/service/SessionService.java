package iu.piisj.eventmanager.service;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.repository.SessionRepository;
import iu.piisj.eventmanager.session.Session;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class SessionService {

    @Inject
    private SessionRepository sessionRepository;

    @Inject
    private EventRepository eventRepository;

    /**
     * Prüfe, ob ein User berechtigt ist, eine Session für ein Event zu erstellen
     * Admin darf für alle Events, Organisatoren nur für ihre eigenen Events
     */
    public boolean canCreateSession(User user, Long eventId) {
        if (user == null) {
            return false;
        }

        // Admin darf alles
        if (user.getRole() == UserRole.ADMIN) {
            return true;
        }

        // Nur Organisatoren dürfen Sessions erstellen
        if (user.getRole() != UserRole.ORGANISATOR) {
            return false;
        }

        // Organisator darf nur für seine eigenen Events
        Event event = eventRepository.findById(eventId);
        if (event == null || event.getOrganizer() == null) {
            return false;
        }

        return user.getId().equals(event.getOrganizer().getId());
    }

    @OrganizerOnly
    public boolean createSession(Session session, User organizer, Long eventId) {
        // Autorisierungs-Check
        if (!canCreateSession(organizer, eventId)) {
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

    @OrganizerOnly
    public boolean deleteSession(User user, Long sessionId) {
        Session session = sessionRepository.findById(sessionId);
        if (session == null) {
            return false;
        }

        // Admin oder Organizer der Session darf löschen
        if (user.getRole() == UserRole.ADMIN) {
            sessionRepository.delete(session);
            return true;
        }

        if (user.getId().equals(session.getOrganizer().getId())) {
            sessionRepository.delete(session);
            return true;
        }

        return false;
    }
}