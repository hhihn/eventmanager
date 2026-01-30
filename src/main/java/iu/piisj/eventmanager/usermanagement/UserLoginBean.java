package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.auth.UserSessionBean;
import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.repository.UserRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class UserLoginBean {

    private String username;
    private String password;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserSessionBean userSession;

    public String login() {

        User user = userRepository.findByUsername(username);

        if (user == null ||
                !PWUtil.verifyPassword(password, user.getPassword())) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Login fehlgeschlagen",
                            "Benutzername oder Passwort falsch"
                    )
            );
            return null;
        }

        userSession.setUser(user);

        // Passwort verwerfen
        password = null;

        return "/index.xhtml?faces-redirect=true";
    }

    public String logout() {
        userSession.logout();
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    // Getter / Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}