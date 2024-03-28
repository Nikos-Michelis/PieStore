<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>


<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>
    <main>
        <ul>
            <li><a href="admin?action=1">Deleting users who have not verified their registration(${requestScope['adminStats'][0]}) registers</a></li>
            <li><a href="admin?action=2">All activation codes for verified records to be set to NULL(${requestScope['adminStats'][1]}) registers</a></li>
        </ul>
    </main>
    <%@ include file="/WEB-INF/segments/footer.jspf"%>
</div>
</body>


</html>
