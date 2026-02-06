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

    private static final String LOGIN_PAGE = "/login.xhtml";

    private static final List<String> PUBLIC_PAGES = List.of(
            "/login.xhtml",
            "/register.xhtml",
            "/index.xhtml"
    );

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws ServletException, IOException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();      // z.B. /eventmanager/orga/create-event.xhtml
        String context = req.getContextPath(); // z.B. /eventmanager
        String path = uri.substring(context.length()); // z.B. /orga/create-event.xhtml

        // JSF resources IMMER erlauben
        boolean isResource = path.startsWith("/jakarta.faces.resource/");
        if (isResource) {
            chain.doFilter(request, response);
            return;
        }

        // Public pages IMMER erlauben
        boolean isPublicPage = PUBLIC_PAGES.contains(path);
        if (isPublicPage) {
            chain.doFilter(request, response);
            return;
        }

        // erst ab hier: Login erforderlich
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(context + LOGIN_PAGE);
            return;
        }

        // Orga-Seiten schützen
        boolean isOrgaPage = path.startsWith("/orga/");
        if (isOrgaPage && !user.isOrgaOrAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}
