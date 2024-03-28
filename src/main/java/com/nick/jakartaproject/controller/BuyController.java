package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.form.FormOrder;
import com.nick.jakartaproject.email.Email;
import com.nick.jakartaproject.models.dao.AreaDAO;
import com.nick.jakartaproject.models.dao.OrderDAO;
import com.nick.jakartaproject.models.dao.PieDAO;
import com.nick.jakartaproject.models.domain.OrderItem;
import com.nick.jakartaproject.models.domain.Pie;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@WebServlet("/buy")
public class BuyController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // get session
        try {
            Cookie[] cookies = request.getCookies();
            System.out.println("Cookie: " + cookies);
            User userFromCookie = UserUtils.getUserFromCookie(cookies);
            if (userFromCookie != null) {
                UserUtils.updateCookieAndSession(request, response, userFromCookie);
                request.setAttribute("areas", AreaDAO.getAreas());
                request.setAttribute("pies", PieDAO.getPies());
                var recentOrders = OrderDAO.recentOrdersOfUser(userFromCookie, 5);
                request.setAttribute("previousOrders", recentOrders);

                String previousOrder = request.getParameter("previousorder");
                if (previousOrder != null) {
                    int prev = Integer.parseInt(previousOrder);
                    for (Map.Entry<String, Integer> entry : recentOrders.get(prev).getOrderItems().entrySet()) {
                        String item = entry.getKey();
                        int quantity = entry.getValue();

                        System.out.println(item);
                        System.out.println(quantity);

                        request.setAttribute(item, quantity);
                    }
                }
                getServletContext()
                        .getRequestDispatcher("/WEB-INF/templates/buy.jsp")
                        .forward(request, response);

            } else {
                request.getSession().invalidate();
                request.setAttribute("errors", "You must Login to access this resource");
                getServletContext()
                        .getRequestDispatcher("/WEB-INF/templates/login.jsp")
                        .forward(request, response);
            }

        } catch (SQLException | NamingException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // read form data
        boolean offerSelected = request.getParameter("offer1") != null;

        FormOrder formOrder = new FormOrder(
                request.getParameter("name"),
                request.getParameter("address"),
                Integer.parseInt(request.getParameter("area")),
                request.getParameter("email"),
                request.getParameter("tel"),
                request.getParameter("message"),
                null,
                offerSelected,
                request.getParameter("payment"),
                LocalDateTime.now()
        );

        List<Pie> pies = PieDAO.getPies();

        List<OrderItem> orderItems = new ArrayList<>();

        for(var pie: pies) {
            /* gets pies quantity by name*/
            int quantity = Integer.parseInt(request.getParameter("Order" + pie.getName()));
            if(quantity > 0) {
                orderItems.add(new OrderItem(null, pie.getId(), null, quantity));
                System.out.println("Pie: " + pie.getName() + ", Quantity: " + quantity);
            }
        }

        int totalPies = 0;
        for (OrderItem orderItem: orderItems) {
            System.out.println(orderItem.getQuantity());
            totalPies += orderItem.getQuantity();
        }

        if (totalPies >= 10) {
            if (offerSelected) {
                formOrder.setOffer(true);
            } else {
                formOrder.setOffer(false);
            }
        } else {
            formOrder.setOffer(false);
        }
        formOrder.setOrderItems(orderItems);

        // formOrder validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<FormOrder>> errors = validator.validate(formOrder);

        if (errors.isEmpty()) { // no errors
            // send e-mails
            Email.sendEmailToClientOrder(formOrder);
            Email.sendEmailToAdminOrder(formOrder);
            // store to db
            OrderDAO.storeOrder(formOrder, (User) request.getSession().getAttribute("user"));
            System.out.println("Done");
            // response
            request.setAttribute("areas", AreaDAO.getAreas());
            request.setAttribute("pies", PieDAO.getPies());
            request.setAttribute("success", true);
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/buy.jsp")
                    .forward(request, response);

        } else { // errors
            StringBuilder errorMessage = new StringBuilder("" +
                    "<p>The form contains the following errors:</p>" +
                    "<ul>");
            for (var error : errors) {
                errorMessage.append("<li>" + error.getMessage() + "</li>");
            }
            errorMessage.append("</ul>");

            request.setAttribute("areas", AreaDAO.getAreas());
            request.setAttribute("pies", PieDAO.getPies());
            request.setAttribute("errors", errorMessage);
            request.setAttribute("formOrder", formOrder);

            // response
            getServletContext()
                    .getRequestDispatcher("/WEB-INF/templates/buy.jsp")
                    .forward(request, response);
        }
    }
}