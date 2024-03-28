package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (UserDAO.logout(request.getSession().getId())) {
            request.setAttribute("success", true);
           // request.getSession().setAttribute("user", null);
            request.getSession().invalidate();
           // request.getSession().setAttribute("isLoggedIn", false);
            // store new session id after login and if exists an old session id then replace that session id in database
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/logout.jsp")
                    .forward(request, response);
        } else { // errors
            StringBuilder errorMessage = new StringBuilder(" " + "<ul>");

            errorMessage.append("<li>" + "No connection has been made" + "</li>");

            errorMessage.append("</ul>");

            request.setAttribute("errors", errorMessage);

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/logout.jsp")
                    .forward(request, response);
        }
    }
}
