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
import java.time.LocalDateTime;
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

        Optional<EventSignup> existing = signupRepository.findByUserAndEvent(userId, eventId);

        if (existing.isPresent()) {
            EventSignup signup = existing.get();

            // If already registered, don't allow duplicate signup
            if (signup.getStatus() == SignupStatus.REGISTERED) {
                return false;
            }

            // If cancelled, allow re-registration by resetting status to REGISTERED
            if (signup.getStatus() == SignupStatus.CANCELLED) {
                signup.setStatus(SignupStatus.REGISTERED);
                signup.setSignupDate(LocalDateTime.now()); // Update signup date to now
                signupRepository.save(signup);
                return true;
            }
        }

        // Create new signup if none exists
        EventSignup signup = new EventSignup(user, event);
        signupRepository.save(signup);
        return true;
    }

    public boolean cancelSignup(Long userId, Long eventId) {
        Optional<EventSignup> signup = signupRepository.findByUserAndEvent(userId, eventId);

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