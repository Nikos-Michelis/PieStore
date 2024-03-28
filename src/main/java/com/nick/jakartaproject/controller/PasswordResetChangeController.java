package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormResetPassword;
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
import java.util.Set;

import static com.nick.jakartaproject.models.dao.UserDAO.updateUsersNewPassword;

@WebServlet("/change-password")
public class PasswordResetChangeController extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String code = request.getParameter("code");
        request.setAttribute("code", code);
        getServletContext()
                .getRequestDispatcher("/WEB-INF/templates/reset-password-change.jsp")
                .forward(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String code = request.getParameter("code");
        FormResetPassword formData = new FormResetPassword(password, repeatPassword);
        // FormResetPassword validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FormResetPassword>> errors = validator.validate(formData);

        if (errors.isEmpty()) { // no errors
            updateUsersNewPassword(password, code);
            request.setAttribute("success", true);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/reset-password-change.jsp")
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
                    .getRequestDispatcher("/WEB-INF/templates/reset-password-change.jsp")
                    .forward(request, response);
        }
    }
}
