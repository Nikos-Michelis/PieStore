package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormRegister;
import com.nick.jakartaproject.models.domain.User;
import com.nick.jakartaproject.models.dao.UserDAO;
import com.nick.jakartaproject.utils.UserUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import static com.nick.jakartaproject.email.Email.sendAuthenticationEmailToClient;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);

            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);
                getServletContext()
                        .getRequestDispatcher("/WEB-INF/templates/buy.jsp")
                        .forward(request, response);

            }else{
                String code = request.getParameter("code");

                if (code != null) {
                    boolean success = UserDAO.isVerifedUser(code);
                    request.setAttribute("RegisterComplete", success);
                    getServletContext()
                            .getRequestDispatcher("/WEB-INF/templates/register.jsp")
                            .forward(request, response);
                } else {
                    getServletContext()
                            .getRequestDispatcher("/WEB-INF/templates/register.jsp")
                            .forward(request, response);
                }
            }
        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String tel = request.getParameter("tel");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");

        FormRegister formData = new FormRegister(fullName, email, tel, userName, password, repeatPassword);
        // FormRegister validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FormRegister>> errors = validator.validate(formData);

        if (errors.isEmpty()) { // no errors
            // response
            int id = UserDAO.storeUserUnverified(formData);
            User user = UserDAO.getUserById(id);
            sendAuthenticationEmailToClient(user);
            System.out.println(formData);
            request.setAttribute("success", true);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/register.jsp")
                    .forward(request, response);


        }
        else { // errors
            StringBuilder errorMessage = new StringBuilder(" " +
                    "<p>The form has the following errors:</p>" + "<ul>");
            for (var error: errors) {
                errorMessage.append("<li>" + error.getMessage() + "</li>");
            }
            errorMessage.append("</ul>");

            request.setAttribute("errors", errorMessage);
            request.setAttribute("formData", formData);

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/register.jsp")
                    .forward(request, response);
        }
    }
}
