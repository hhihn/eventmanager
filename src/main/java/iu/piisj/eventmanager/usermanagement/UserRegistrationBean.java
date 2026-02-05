package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.dto.UserRegistrationDTO;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import iu.piisj.eventmanager.repository.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class UserRegistrationBean implements Serializable {

    @Inject
    private UserRepository userRepository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    private UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

    public List<UserRole> getAvailableRoles() {
        return List.of(UserRole.values());
    }

    public void register() {

        if (userRepository.emailExists(userRegistrationDTO.getEmail())) {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Fehler",
                            "Diese E-Mail Adresse ist bereits registriert."
                    )
            );
            return;
        }

        String hashedPassword =
                passwordHash.generate(
                        userRegistrationDTO.getPassword().toCharArray()
                );

        userRegistrationDTO.setPassword(hashedPassword);

        User user = new User(userRegistrationDTO);
        userRepository.save(user);

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Erfolg",
                        "Registrierung war erfolgreich"
                )
        );

        userRegistrationDTO = new UserRegistrationDTO();
    }

    public UserRegistrationDTO getUserRegistrationDTO() {
        return userRegistrationDTO;
    }

    public void setUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO) {
        this.userRegistrationDTO = userRegistrationDTO;
    }
}