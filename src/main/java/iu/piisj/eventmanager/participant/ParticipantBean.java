package iu.piisj.eventmanager.participant;

import iu.piisj.eventmanager.accessmanagement.OrganizerOnly;
import iu.piisj.eventmanager.repository.UserRepository;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserLoginBean;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ParticipantBean implements Serializable {


    @Inject
    private UserRepository userRepository;

    @Inject
    private UserLoginBean userLoginBean;

    @OrganizerOnly
    public List<User> getParticipants() {
        return userRepository.findAll();
    }

    public boolean isAdmin() {
        User currentUser = userLoginBean.getLoggedInUser();
        return currentUser != null && currentUser.getRole() == UserRole.ADMIN;
    }
}