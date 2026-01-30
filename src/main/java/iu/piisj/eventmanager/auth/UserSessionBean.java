package iu.piisj.eventmanager.auth;

import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }



    public void login(User user) {
        this.currentUser = user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean hasRole(UserRole role) {
        return isLoggedIn() && currentUser.getRole() == role;
    }

    public boolean isAdmin() {
        return hasRole(UserRole.ADMIN);
    }

    public boolean isOrganisator() {
        return hasRole(UserRole.ORGANISATOR);
    }

    public boolean isTeilnehmer() {
        return hasRole(UserRole.TEILNEHMER);
    }
}