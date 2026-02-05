package iu.piisj.eventmanager.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                errorPage = "/login.xhtml?error=true"
        )
)

@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "java:comp/env/jdbc/eventdb",
        callerQuery = "SELECT password FROM users WHERE username = ?",
        groupsQuery = "SELECT role FROM users WHERE username = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class
)

@ApplicationScoped
public class SecurityConfig {
    // bewusst leer
}