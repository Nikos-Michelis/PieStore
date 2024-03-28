<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="formData" class="com.nick.jakartaproject.form.FormRegister" scope="request" />
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <h1>Register</h1>
        <article>
            <c:choose>
                <c:when test="${requestScope['success']}">
                    <div class="success-message">
                        Successful Registration! Log in to your email for the necessary verification!
                    </div>
                </c:when>
                <c:when test="${requestScope['RegisterComplete'] == true}">
                    <div class="success-message">
                        Your account has been verified! Proceed to <a href="login">login</a>
                    </div>
                </c:when>
                <c:when test="${requestScope['RegisterComplete'] == false}">
                    <div class="error-message">
                        <a>Failure to verify registration please proceed to registration again <a href="register">register</a></a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:if test="${requestScope['errors']!=null}">
                        <div class="error-message">${requestScope['errors']} </div>
                    </c:if>
                    <form action="register" method="POST" class="form contact">
                        <label for="txtName">FullName(*):</label>
                        <input type="text" id="txtName" name="fullName" minlength="3" maxlength="40" size="40" value="${formData.fullName}" required>

                        <label for="txtEmail">E-mail(*): </label>
                        <input type="email" id="txtEmail" name="email" size="40" value="${formData.email}" required>

                        <label for="txtTel">Phone: </label>
                        <input type="tel" id="txtTel" name="tel" size="10" value="${formData.tel}">

                        <label for="txtUserName">Username(*):</label>
                        <input type="text" id="txtUserName" name="userName" maxlength="30" value="${formData.userName}" required>

                        <label for="txtPassword">Password(*): </label>
                        <input type="password" id="txtPassword" name="password" maxlength="30" required>

                        <label for="txtRepeatPassword">Repeat Password(*): </label>
                        <input type="password" id="txtRepeatPassword" name="repeatPassword" maxlength="30" required>

                        <span>(*) The fields are mandatory</span>
                        <input type="submit" value="Register">
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
