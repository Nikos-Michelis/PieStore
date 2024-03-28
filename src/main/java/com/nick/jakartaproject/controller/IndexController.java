package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.models.domain.User;
import com.nick.jakartaproject.utils.UserUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"", "/life-cycle"})
public class IndexController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Check if the "appdata" cookie is existed in browser
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);

            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);

            }
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/index.jsp")
                    .forward(request, response);

        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

}