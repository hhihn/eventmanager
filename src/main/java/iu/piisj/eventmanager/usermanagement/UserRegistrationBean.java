package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.dto.UserRegistrationDTO;
import iu.piisj.eventmanager.repository.UserRepository;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UserRegistrationBean implements Serializable {

    // Inject: wenn ein Objekt dieser Klasse erzeugt wird, dann wird auch ein Objekt
    // der Klasse UserRepository erzeugt und hier eingefügt
    // wichtig: zur Laufzeit unsrer Applikation passiert das vollautomatisch. Deswegen muss es einen
    // "leeren" Konstruktor dieser Klasse geben (also einen ohne Argumente).
    // Die Klasse ist somit stateless.
    // Dependency Injection
    @Inject
    private UserRepository userRepository;

    private UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO();

    public List<UserRole> getAvailableRoles() {
        return List.of(UserRole.values());
    }

    public void register() {

        // 1. überprüfe ob die email adresse schon registriert ist
        if (userRepository.emailExists(userRegistrationDTO.getEmail())){
            // falls ja, dann sende eine Warnung zurück
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Fehler",
                    "Diese E-Mail Adresse ist bereits registriert."
            ));

            return;
        }

        userRegistrationDTO.setPassword(PWUtil.hashPassword(userRegistrationDTO.getPassword()));
        // übersetze DTO zur Entity
        User user = new User(userRegistrationDTO);
        // Speichere Entity in der Datenbank
        userRepository.save(user);
        // informiere den User über erfolgreiche Aktion
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "Erfolg",
                "Registrierung war erfolgreich"
        ));
        // resette das DTO für zukünftige Transaktionen
        userRegistrationDTO = new UserRegistrationDTO();
    }

    public UserRegistrationDTO getUserRegistrationDTO() {
        return userRegistrationDTO;
    }

    public void setUserRegistrationDTO(UserRegistrationDTO userRegistrationDTO) {
        this.userRegistrationDTO = userRegistrationDTO;
    }
}
