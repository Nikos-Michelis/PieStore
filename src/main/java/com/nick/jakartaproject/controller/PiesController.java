package com.nick.jakartaproject.controller;

import com.nick.jakartaproject.models.domain.Pie;
import com.nick.jakartaproject.models.dao.PieDAO;
import com.nick.jakartaproject.utils.UserUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/pies")
public class PiesController extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // get url parameters
        System.out.println("Pies session: " + UserUtils.getUserSession(request) );

        // prepare the data for the response(model -> prepare data)
        List<Pie> pieList = PieDAO.getPies();
        // set the bean object as parameter to response
        request.setAttribute("piesBean", pieList);
        System.out.println("Stock List:" + pieList);

        getServletContext()
                .getRequestDispatcher("/WEB-INF/templates/pies.jsp")
                .forward(request, response);

    }
}