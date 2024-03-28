package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormLogin;
import com.nick.jakartaproject.models.domain.User;
import com.nick.jakartaproject.utils.UserUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

@WebServlet("/login")
public class LoginController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);

            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);

            }
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                    .forward(request, response);

        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Check if the user is already logged in
            User existingUser = UserUtils.getUserSession(request);
            System.out.println("existingUser: " + existingUser);
            if (existingUser != null) {
                // User is already logged in, redirect to a welcome page or handle as needed
                response.sendRedirect("/buy");
            }

            // Your existing form validation code
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            System.out.println("userName: " + userName);
            System.out.println("password: " + password);

            FormLogin formData = new FormLogin(userName.trim(), password.trim());
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<FormLogin>> errors = validator.validate(formData);


            if (errors.isEmpty()) { // no errors
                request.setAttribute("success", true);

                // Check if the "appdata" cookie is existed in browser
                Cookie[] cookies = request.getCookies();
                System.out.println("Cookie: " + cookies);
                User userFromCookie = UserUtils.getUserFromCookie(cookies);

                if (userFromCookie != null) {
                    // Log in the user based on the existing cookie
                    UserUtils.updateCookieAndSession(request, response, userFromCookie);

                    getServletContext()
                            .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                            .forward(request, response);
                }else {
                    // Create new user session and cookie
                    UserUtils.createSessionAndCookie(request, response, userName);

                    getServletContext()
                            .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                            .forward(request, response);
                }
            } else {
                StringBuilder errorMessage = new StringBuilder(" " + "<ul>");
                for (var error : errors) {
                    errorMessage.append("<li>" + error.getMessage() + "</li>");
                }
                errorMessage.append("</ul>");

                request.setAttribute("errors", errorMessage);
                request.setAttribute("formData", formData);

                getServletContext()
                        .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                        .forward(request, response);
            }
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
}

