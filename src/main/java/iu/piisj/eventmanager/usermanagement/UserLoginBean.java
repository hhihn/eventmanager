package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.repository.UserRepository;
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

    private User loggedInUser = null;

    @Inject
    private UserRepository userRepository;

    public String login() {
        System.out.println("LOGIN AUFGERUFEN: " + username);
        User user = userRepository.findByUsername(username);

        if (user == null ||
                !PWUtil.verifyPassword(password, user.getPassword())) {
            System.out.println("LOGIN FEHLGESCHLAGEN: " + username);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login fehlgeschlagen",
                            "Benutzername oder Passwort falsch"));

            return null; // bleibe auf Login-Seite
        }
        System.out.println("USER GEFUNDEN " + username);
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("user", user);
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
        System.out.println("LOGIN STATUS: " + (loggedInUser != null));
        return loggedInUser != null;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public User getLoggedInUser() { return loggedInUser; }
}