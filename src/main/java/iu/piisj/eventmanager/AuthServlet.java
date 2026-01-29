package iu.piisj.eventmanager;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import iu.piisj.eventmanager.usermanagement.User;

@WebFilter("/*")
public class AuthServlet implements Filter {

    private static final String LOGIN_PAGE = "/login.xhtml";

    private static final List<String> PUBLIC_PAGES = List.of(
            "/login.xhtml",
            "/register.xhtml",
            "/index.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String context = req.getContextPath();

        User user = (User) req.getSession().getAttribute("user");

        boolean loggedIn = user != null;

        boolean isPublicPage = PUBLIC_PAGES.stream()
                .anyMatch(path::endsWith);

        boolean isResource =
                path.startsWith(context + "/jakarta.faces.resource");

        if (loggedIn || isPublicPage || isResource) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(context + LOGIN_PAGE);
        }
    }
}