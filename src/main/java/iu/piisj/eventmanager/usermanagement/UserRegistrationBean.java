package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.dto.UserRegistrationDTO;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import iu.piisj.eventmanager.repository.UserRepository;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named
@ViewScoped
public class UserRegistrationBean implements Serializable {

    @Inject
    private UserRepository userRepository;

    private UserRegistrationDTO registration = new UserRegistrationDTO();

    public UserRegistrationDTO getRegistration() {
        return registration;
    }

    public List<UserRole> getAvailableRoles() {
        return Arrays.asList(UserRole.values());
    }

    public void register() {

        if (userRepository.emailExists(registration.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Fehler",
                            "Diese E-Mail-Adresse ist bereits registriert."
                    )
            );
            return;
        }

        User user = new User(
                registration.getUsername(),
                registration.getEmail(),
                registration.getPassword(), // später: Hashing!
                registration.getRole()
        );

        userRepository.save(user);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Erfolg",
                        "Registrierung erfolgreich."
                )
        );

        registration = new UserRegistrationDTO();
    }
}