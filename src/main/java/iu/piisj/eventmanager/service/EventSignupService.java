package iu.piisj.eventmanager.service;

import iu.piisj.eventmanager.event.Event;
import iu.piisj.eventmanager.eventsignup.EventSignup;
import iu.piisj.eventmanager.eventsignup.SignupStatus;
import iu.piisj.eventmanager.repository.EventRepository;
import iu.piisj.eventmanager.repository.EventSignupRepository;
import iu.piisj.eventmanager.repository.UserRepository;
import iu.piisj.eventmanager.usermanagement.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class EventSignupService {

    @Inject
    private EventSignupRepository signupRepository;

    @Inject
    private EventRepository eventRepository;

    @Inject
    private UserRepository userRepository;

    public boolean signupUserForEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId);
        Event event = eventRepository.findById(eventId);

        if (user == null || event == null) {
            return false;
        }

        // Check if already signed up
        Optional<EventSignup> existing =
                signupRepository.findByUserAndEvent(userId, eventId);
        if (existing.isPresent()) {
            return false; // Already registered
        }

        EventSignup signup = new EventSignup(user, event);
        signupRepository.save(signup);
        return true;
    }

    public boolean cancelSignup(Long userId, Long eventId) {
        Optional<EventSignup> signup =
                signupRepository.findByUserAndEvent(userId, eventId);

        if (signup.isEmpty()) {
            return false;
        }

        signup.get().setStatus(SignupStatus.CANCELLED);
        signupRepository.save(signup.get());
        return true;
    }

    public boolean isUserSignedUp(Long userId, Long eventId) {
        return signupRepository.findByUserAndEvent(userId, eventId)
                .map(s -> s.getStatus() == SignupStatus.REGISTERED)
                .orElse(false);
    }
}