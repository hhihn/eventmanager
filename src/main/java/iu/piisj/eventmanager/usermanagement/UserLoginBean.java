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

    // man könnte auch ein UserLoginDTO erstellen
    // aber weil es nur zwei werte sind, sparen wir uns das
    private String username;
    private String password;

    private User loggedInUser;

    @Inject
    private UserRepository userRepository;

    public String login() {

        User user = userRepository.findByUsername(username);

        if (user == null || !PWUtil.verifyPassword(password, user.getPassword())){
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Login fehlgeschlagen.",
                            "Benutzername oder Passwort ist falsch."));
            return null; // verleibe auf der Login Seite
        }
        // kommt man hier an, dann gibt es den User und das PW ist korrekt
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Login war erfolgreich",
                        "Login war erfolgreich"));

        // Speichere den User in die Session
        loggedInUser = user;
        password = null;
        FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("user", loggedInUser);
         // Path Variable
        return "/index.xhtml?faces-redirect=true";
    }

    public String logout(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn(){
        return loggedInUser != null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
