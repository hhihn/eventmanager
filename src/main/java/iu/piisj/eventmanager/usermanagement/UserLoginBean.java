package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.repository.UserRepository;
import iu.piisj.eventmanager.usermanagement.PWUtil;
import iu.piisj.eventmanager.usermanagement.User;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class UserLoginBean implements Serializable {

    private String username;
    private String password;

    private User loggedInUser;

    @Inject
    private UserRepository userRepository;

    public String login() {

        User user = userRepository.findByUsername(username);

        if (user == null ||
                !PWUtil.verifyPassword(password, user.getPassword())) {

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login fehlgeschlagen",
                            "Benutzername oder Passwort falsch"));

            return null; // bleibe auf Login-Seite
        }

        loggedInUser = user;
        password = null; // Sicherheit

        return "/index.xhtml?faces-redirect=true";
    }

    public String logout() {
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public boolean hasRole(String role) {
        return isLoggedIn() && loggedInUser.getRole().equals(role);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public User getLoggedInUser() { return loggedInUser; }
}