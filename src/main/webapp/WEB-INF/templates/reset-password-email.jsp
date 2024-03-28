<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="formData" class="com.nick.jakartaproject.form.FormResetEmail" scope="request" />
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <h1>Send password reset email</h1>
        <article>
            <c:choose>
                <c:when test="${requestScope['success']}">
                    <div class="success-message">
                        The password reset email has been sent with good luck!
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${requestScope['errors']!=null}">
                        <div class="error-message">${requestScope['errors']} </div>
                    </c:if>
                    <div><p>Enter the e-mail with which you registered: </p></div>
                    <form action="password-reset-email" method="POST" class="form contact">

                        <label for="txtEmail">Email: </label>
                        <input type="email" id="txtEmail" name="email" size="40"required>

                        <input type="submit" value="Send">
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
