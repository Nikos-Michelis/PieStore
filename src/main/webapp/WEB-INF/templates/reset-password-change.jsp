<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="formData" class="com.nick.jakartaproject.form.FormResetPassword" scope="request" />
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <h1>Reset Password</h1>
        <article>
            <c:choose>
                <c:when test="${requestScope['success']}">
                    <div class="success-message">
                        Your password has been successfully changed! Go to <a href="login">login</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${requestScope['errors']!=null}">
                        <div class="error-message">${requestScope['errors']} </div>
                    </c:if>
                    <form action="change-password" method="POST" class="form contact">
                        <label for="txtPassword">Password(*): </label>
                        <input type="password" id="txtPassword" name="password" maxlength="30" required>

                        <label for="txtRepeatPassword">Repeat Password(*): </label>
                        <input type="password" id="txtRepeatPassword" name="repeatPassword" maxlength="30" required>

                        <input type="hidden" name="code" value="${requestScope['code']}">
                        <input type="submit" value="Reset Password">
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
