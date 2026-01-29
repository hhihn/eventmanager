package iu.piisj.eventmanager;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import iu.piisj.eventmanager.usermanagement.UserLoginBean;

@WebFilter("/*")
public class AuthServlet implements Filter {

    private static final String LOGIN_PAGE = "/login.xhtml";

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String context = req.getContextPath();

        // LoginBean aus Session holen
        UserLoginBean loginBean = (UserLoginBean)
                req.getSession().getAttribute("loginBean");

        boolean loggedIn = loginBean != null && loginBean.isLoggedIn();

        boolean isLoginPage = path.endsWith("login.xhtml");
        boolean isResource  = path.startsWith(context + "/jakarta.faces.resource");

        if (loggedIn || isLoginPage || isResource) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect(context + LOGIN_PAGE);
        }
    }
}