package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormContact;
import com.nick.jakartaproject.models.domain.User;
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

import static com.nick.jakartaproject.email.Email.sendEmailToAdminContactForm;
import static com.nick.jakartaproject.email.Email.sendEmailToClientContactForm;

@WebServlet("/contact")
public class ContactController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);

            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);

            }
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/contact.jsp")
                    .forward(request, response);

        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        User user = (User) request.getSession().getAttribute("user");
        String message = request.getParameter("message");
        FormContact formData = new FormContact(user.getUserName(), user.getEmail(), user.getTel(), message);
        System.out.println("FormContact: " + formData);
        // create a validation factory (Prepare object for validation)
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        // error messages from annotations
        Set<ConstraintViolation<FormContact>> errors = validator.validate(formData);
        // if there not exists any error then make the login
        if (errors.isEmpty()) { // no errors
            System.out.println(formData.getFullname() + " " +
                    formData.getEmail() + " "
                    + formData.getTel() + " "
                    + formData.getMessage());
            // response
            sendEmailToClientContactForm(formData);
            sendEmailToAdminContactForm(formData);
            request.setAttribute("success", true);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/contact.jsp")
                    .forward(request, response);


        }
        else { // errors
            StringBuilder errorMessage = new StringBuilder(" " +
                    "<p>The form contains the following errors:</p>" + "<ul>");
            for (var error: errors) {
                errorMessage.append("<li>" + error.getMessage() + "</li>");
            }
            errorMessage.append("</ul>");

            request.setAttribute("errors", errorMessage);
            request.setAttribute("formData", formData);

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/contact.jsp")
                    .forward(request, response);
        }
    }
}