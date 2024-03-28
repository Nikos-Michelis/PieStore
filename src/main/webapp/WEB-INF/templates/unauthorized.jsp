<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <article>
            <c:if test="${requestScope['errors']!=null}">
                <div class="error-message">${requestScope['errors']} </div>
            </c:if>
        </article>
        <%@ include file="/WEB-INF/segments/aside.jspf"%>
    </main>
    <%@ include file="/WEB-INF/segments/footer.jspf"%>
</div>
</body>
</html>
