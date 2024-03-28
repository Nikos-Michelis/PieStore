package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.models.domain.Pie;
import com.nick.jakartaproject.models.dao.PieDAO;
import com.nick.jakartaproject.models.domain.User;
import com.nick.jakartaproject.utils.UserUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
@WebServlet("/pie")
public class PieController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);

            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);
            }

            // get url parameters
            String idParam = request.getParameter("id");

            if (idParam == null || idParam.isEmpty()) {
                // Handle the case when the "id" parameter is missing or empty
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "The 'id' parameter is missing or empty.");
                return;
            }

            int id = Integer.parseInt(idParam);

            // prepare the data for the response (model -> prepare data)
            Pie pie = PieDAO.getPieById(id);

            if (pie == null) {
                // Handle the case when the requested pie is not found
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Pie with id " + id + " not found.");
                return;
            }
            request.setAttribute("bean", pie);

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/pie.jsp")
                    .forward(request, response);
        } catch (SQLException | NamingException | NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
            e.printStackTrace();
        }
    }
}