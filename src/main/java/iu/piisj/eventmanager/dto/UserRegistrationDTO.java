package iu.piisj.eventmanager.dto;
import iu.piisj.eventmanager.usermanagement.UserRole;

public class UserRegistrationDTO {

    private String username;
    private String name;
    private String firstname;
    private String email;
    private String state;

    private String password;
    private UserRole role;

    public UserRegistrationDTO(String username, String name, String firstname, String email, String state, String password, UserRole role) {
        this.username = username;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.state = state;
        this.password = password;
        this.role = role;
    }

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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}