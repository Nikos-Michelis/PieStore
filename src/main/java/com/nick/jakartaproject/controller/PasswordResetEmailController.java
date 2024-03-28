package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormResetEmail;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.io.IOException;
import java.util.Random;
import java.util.Set;

import static com.nick.jakartaproject.email.Email.sendResetPasswordEmailToClient;
import static com.nick.jakartaproject.models.dao.UserDAO.updateUsersResetCode;

@WebServlet("/password-reset-email")
public class PasswordResetEmailController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        getServletContext()
                .getRequestDispatcher("/WEB-INF/templates/reset-password-email.jsp")
                .forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String email = request.getParameter("email");

        FormResetEmail formData = new FormResetEmail(email);
        // FormResetEmail validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FormResetEmail>> errors = validator.validate(formData);

        if (errors.isEmpty()) { // no errors
            // response
            Random random = new Random();
            int code = random.nextInt(100000);
            sendResetPasswordEmailToClient(email, String.valueOf(code));
            updateUsersResetCode(email, code);
            request.setAttribute("success", true);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/reset-password-email.jsp")
                    .forward(request, response);

        }
        else { // errors
            StringBuilder errorMessage = new StringBuilder(" " + "<ul>");
            for (var error: errors) {
                errorMessage.append("<li>" + error.getMessage() + "</li>");
            }
            errorMessage.append("</ul>");

            request.setAttribute("errors", errorMessage);
            request.setAttribute("formData", formData);

            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/reset-password-email.jsp")
                    .forward(request, response);
        }
    }
}
