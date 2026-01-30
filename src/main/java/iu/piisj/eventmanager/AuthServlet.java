package iu.piisj.eventmanager;


import iu.piisj.eventmanager.usermanagement.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class AuthServlet implements Filter {

    // hier wird die login page definiert
    // das brauchen wir damit der filter unerlaubte anfragen auf diese seite umleitet
    private static final String LOGIN_PAGE = "/login.xhtml";

    // hier sind alle xhtml Seiten, die ohne Login angefragt werden können
    private static final List<String> PUBLIC_PAGES = List.of(
            "/login.xhtml",
            "/register.xhtml",
            "/index.xhtml"
    );

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // welchen Pfad fragt die Request an?
        String path = req.getRequestURI();
        String context = req.getContextPath();

        // wer fragt den pfad an?
        User user = (User) req.getSession().getAttribute("user");

        // ist der user eingeloggt?
        boolean loggedIn = user != null;

        // ist der Pfad teil der öffentlichen Seiten?
        boolean isPublicPage = PUBLIC_PAGES.stream().anyMatch(path::endsWith);

        // fragt der user eine JSF resource an?
        boolean isResource = path.startsWith(context + "/jakarta.faces.resource");

        // ist der user eingeloggt, oder fragt er eine resource an oder fragt er eine öffentliche Page an?
        if (loggedIn || isResource || isPublicPage){
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(context + LOGIN_PAGE);
        }

    }

}
