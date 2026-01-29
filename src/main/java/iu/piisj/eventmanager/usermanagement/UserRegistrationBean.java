package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.dto.UserRegistrationDTO;
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
                            "Diese E-Mail-Adresse ist bereits registriert."
                    )
            );
            return;
        }
        String hashedPassword =
                PWUtil.hashPassword(userRegistrationDTO.getPassword());
        User user = new User(
                userRegistrationDTO.getUsername(),
                userRegistrationDTO.getName(),
                userRegistrationDTO.getFirstname(),
                userRegistrationDTO.getEmail(),
                userRegistrationDTO.getState(),
                hashedPassword,
                userRegistrationDTO.getRole()
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

        userRegistrationDTO = new UserRegistrationDTO();
    }

    public UserRegistrationDTO getUserRegistrationDTO() {
        return userRegistrationDTO;
    }

    public void setUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO) {
        this.userRegistrationDTO = userRegistrationDTO;
    }
}