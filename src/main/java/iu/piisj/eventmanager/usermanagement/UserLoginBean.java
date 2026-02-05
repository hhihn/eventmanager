package iu.piisj.eventmanager.usermanagement;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.Serializable;

@Named
@RequestScoped
public class UserLoginBean implements Serializable {

    private String username;
    private String password;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private HttpServletRequest request;

    public String login() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest req = (HttpServletRequest) fc.getExternalContext().getRequest();
        HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();

        AuthenticationStatus status = securityContext.authenticate(
                req,
                resp,
                AuthenticationParameters.withParams()
                        .credential(new UsernamePasswordCredential(username, password))
        );

        // Debug: nur für Entwicklung (Passwort NIE loggen!)
        System.out.println("status=" + status);
        System.out.println("remoteUser=" + req.getRemoteUser());
        System.out.println("principal=" + req.getUserPrincipal());

        switch (status) {
            case SUCCESS -> {
                // Wichtig: bei SUCCESS ist die Auth ok – Redirect auf Zielseite:
                // Wenn LoginToContinue aktiv ist, kann der Container ggf. selbst schon umleiten.
                // Trotzdem ist ein redirect auf index ok, wenn du kein "original request" Handling brauchst.
                return "/index.xhtml?faces-redirect=true";
            }
            case SEND_CONTINUE -> {
                // Container hat schon Redirect/Forward gemacht (z.B. LoginToContinue)
                fc.responseComplete();
                return null;
            }
            case SEND_FAILURE, NOT_DONE -> {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Login fehlgeschlagen", "Benutzername oder Passwort ist falsch."));
                return null;
            }
            default -> {
                fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Login fehlgeschlagen", "Unbekannter Status: " + status));
                return null;
            }
        }
    }

    public String logout() {
        try {
            request.logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    // Container-Status statt eigener loggedInUser-Variable
    public boolean isLoggedIn() {
        return request.getUserPrincipal() != null;
    }

    public boolean isAdmin() {
        return request.isUserInRole("ADMIN");
    }

    public boolean isOrganisator() {
        return request.isUserInRole("ORGANISATOR");
    }

    public boolean isTeilnehmer() {
        return request.isUserInRole("TEILNEHMER");
    }

    // Getter/Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
