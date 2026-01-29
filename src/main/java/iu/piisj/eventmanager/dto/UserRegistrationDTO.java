package iu.piisj.eventmanager.dto;
import iu.piisj.eventmanager.usermanagement.UserRole;

public class UserRegistrationDTO {

    private String username;
    private String email;
    private String password;
    private UserRole role;

    public UserRegistrationDTO() {
    }

    // Getter & Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }
}