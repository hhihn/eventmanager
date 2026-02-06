package iu.piisj.eventmanager.accessmanagement;

import iu.piisj.eventmanager.usermanagement.User;
import iu.piisj.eventmanager.usermanagement.UserRole;
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

        if (!user.isOrgaOrAdmin()) {
            throw new SecurityException("Organizer role required.");
        }

        return ctx.proceed();
    }
}
