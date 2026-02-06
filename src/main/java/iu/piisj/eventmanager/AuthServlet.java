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
        String uri = req.getRequestURI(); // z.B. /eventmanager/orga/create-event.xhtml
        String context = req.getContextPath(); // z.B. /eventmanager
        String path = uri.substring(context.length()); // z.B. /orga/create-event.xhtml
        // wer fragt den pfad an?
        User user = (User) req.getSession().getAttribute("user");

       // JSF Resources erlauben
        boolean isResource = path.startsWith("/jakarta.faces.resource/");
        if (isResource) {
            chain.doFilter(request, response);
            return;
        }

        // Public Pages darf jeder sehen
        boolean isPublicPage = PUBLIC_PAGES.contains(path);
        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // ist der user eingeloggt?
        if (user == null){
            // wenn user nicht eingeloggt ist, dann schicke ihn zur login seite
            resp.sendRedirect(context + LOGIN_PAGE);
        }


        // Besonderen Schutz für die Orga Seiten
        boolean isOrgaPage = path.startsWith("/orga/");
        if (isOrgaPage && !user.isOrgaOrAdmin()){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);

    }

}
