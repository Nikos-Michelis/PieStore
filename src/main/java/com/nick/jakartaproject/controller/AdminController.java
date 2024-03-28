package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin")
public class AdminController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sessionId = request.getSession().getId();
        System.out.println("admin: " + sessionId);
        String role = UserDAO.getRole(sessionId);
        if (role == null) {
            request.setAttribute("errors", "You must Login to access this resource");

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                    .forward(request, response);
        } else if (!role.equals("admin")) {
            request.setAttribute("errors", "You do not have the necessary rights to view this page.");
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/unauthorized.jsp")
                    .forward(request, response);
        }else{
            String action = request.getParameter("action");
            if(action != null){
                if(request.getParameter("action").equals("1")){
                    UserDAO.deleteUnverifiedUsers();
                } else if (request.getParameter("action").equals("2")) {
                    UserDAO.updateVerifiedUsersCode();
                }
            }

            List<Integer> adminStats = UserDAO.getAdminStats();
            request.setAttribute("adminStats", adminStats);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/admin.jsp")
                    .forward(request, response);
        }
    }
}