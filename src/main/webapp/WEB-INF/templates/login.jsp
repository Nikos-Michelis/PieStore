<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="formData" class="com.nick.jakartaproject.form.FormLogin" scope="request" />
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <h1>Login</h1>
        <article>
            <c:choose>
                <c:when test="${requestScope['success']}">
                    <div class="success-message">
                        Successful Connection! Continue browsing the page!
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${requestScope['errors']!=null}">
                        <div class="error-message">${requestScope['errors']} </div>
                    </c:if>
                    <form action="login" method="POST" class="form contact">

                        <label for="txtUserName">Username:</label>
                        <input type="text" id="txtUserName" name="userName" maxlength="30" value="${formData.userName}" required >

                        <label for="txtPassword">Password:</label>
                        <input type="password" id="txtPassword" name="password" maxlength="30" required>

                        <span><a href="password-reset-email">Forgot your password?</a></span>
                        <input type="submit" value="login">
                    </form>
                </c:otherwise>
            </c:choose>
        </article>
        <%@ include file="/WEB-INF/segments/aside.jspf"%>
    </main>
    <%@ include file="/WEB-INF/segments/footer.jspf"%>
</div>
</body>
</html>
