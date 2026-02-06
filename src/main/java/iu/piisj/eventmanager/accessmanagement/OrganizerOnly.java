package iu.piisj.eventmanager.accessmanagement;

import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@InterceptorBinding // was will tun?
@Target({ElementType.METHOD, ElementType.TYPE}) // worauf soll es wirken?
@Retention(RetentionPolicy.RUNTIME) // zu welcher "zeit" soll es wirken?
public @interface OrganizerOnly {
}
