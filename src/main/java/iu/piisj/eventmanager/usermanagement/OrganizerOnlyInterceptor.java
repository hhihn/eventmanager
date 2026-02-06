package iu.piisj.eventmanager.usermanagement;

import jakarta.faces.context.FacesContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@OrganizerOnly
@Interceptor
public class OrganizerOnlyInterceptor {

    @AroundInvoke
    public Object checkAccess(InvocationContext ctx) throws Exception {

        FacesContext faces = FacesContext.getCurrentInstance();

        if (faces == null) {
            throw new SecurityException("No FacesContext available.");
        }

        User user = (User) faces.getExternalContext()
                .getSessionMap()
                .get("user");

        if (user == null) {
            throw new SecurityException("Not logged in.");
        }

        boolean isOrganizer = user.getRole().equals(UserRole.ORGANISATOR) || user.getRole().equals(UserRole.ADMIN);
        System.out.println("USER ROLE" + user.getRole());
        if (!isOrganizer) {
            throw new SecurityException("Organizer role required.");
        }

        return ctx.proceed();
    }
}
