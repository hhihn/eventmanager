package iu.piisj.eventmanager.usermanagement;

import iu.piisj.eventmanager.dto.UserRegistrationDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String name;
    private String firstname;
    private String email;
    private String location;
    private String state;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public User() {}

    public User(String username, String name, String firstname, String email, String location, String state, String password,
                UserRole role) {
        this.username = username;
        this.name = name;
        this.firstname = firstname;
        this.email = email;
        this.location = location;
        this.state = state;
        this.password = password;
        this.role = role;
    }

    public User(UserRegistrationDTO newUserDTO){
        this.username = newUserDTO.getUsername();
        this.name = newUserDTO.getName();
        this.email = newUserDTO.getEmail();
        this.password = newUserDTO.getPassword();
        this.firstname = newUserDTO.getFirstname();
        this.location = newUserDTO.getLocation();
        this.role = newUserDTO.getRole();
        this.state = newUserDTO.getState();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}
