package com.nick.jakartaproject.utils;

import com.nick.jakartaproject.models.domain.User;
import com.nick.jakartaproject.models.dao.UserDAO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.naming.NamingException;
import java.sql.SQLException;

public class UserUtils {
    private static final String APP_DATA_COOKIE = "appdata";

    public static User getUserSession(HttpServletRequest request) {
        String sessionId = request.getSession().getId();
        User user = UserDAO.getUserBySession(sessionId);
        request.getSession().setAttribute("user", user);
        return user;
    }
    public static User getUserFromCookie(Cookie[] cookies) throws SQLException, NamingException {
        Cookie cookie = getCookieByName(cookies, APP_DATA_COOKIE);
        String oldSession = null;
        if (cookie != null) {
            oldSession = cookie.getValue();
        }
        return UserDAO.getUserBySession(oldSession);
    }
    public static void createSessionAndCookie(HttpServletRequest request, HttpServletResponse response, String userName) throws SQLException, NamingException {
        System.out.println("There is not exists any cookie ");
        HttpSession session = request.getSession();
        UserDAO.storeSession(userName, session.getId());
        System.out.println("session_id: " + session.getId());
        Cookie cookieSession = createCookie(response, request);
        User getUserFromCookie = getUserFromCookie(new Cookie[]{cookieSession});
        System.out.println("getUserFromCookie-else: " + getUserFromCookie);
        setSessionAttributes(request, getUserFromCookie);
    }
    public static void updateCookieAndSession(HttpServletRequest request, HttpServletResponse response, User userFromCookie){
        System.out.println("user login with cookie");
        // Log in the user based on the existing cookie
        UserDAO.storeSession(userFromCookie.getUserName(), request.getSession().getId());
        setSessionAttributes(request, userFromCookie);
        createCookie(response, request);
    }
    private static Cookie createCookie(HttpServletResponse response, HttpServletRequest request){
        // save that session to cookie browser
        Cookie cookieSession = new Cookie(APP_DATA_COOKIE, request.getSession().getId());
        //cookieSession.setMaxAge(10); // a month
        cookieSession.setMaxAge(30 * 24 * 60); // a month
        response.addCookie(cookieSession);
        return cookieSession;
    }
    private static void setSessionAttributes(HttpServletRequest request, User userFromCookie){
        String role = UserDAO.getRole(request.getSession().getId());
        request.getSession().setAttribute("user", userFromCookie);
        request.getSession().setAttribute("role", role);
    }
    private static Cookie getCookieByName(Cookie[] cookies, String name) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
