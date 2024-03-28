<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="bean" scope="request" class="com.nick.jakartaproject.models.domain.Pie"/>
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
        <h1>${bean.name}</h1>
        <article>
            <div>
                <img src="${bean.fileName}" alt="image - ${bean.name}"/>
                <p>Ingredients: ${bean.ingredients}</p>
                <p>Price: ${bean.price} €</p>
                <p>Description: ${bean.description}</p>
            </div>
        </article>

        <%@ include file="/WEB-INF/segments/aside.jspf"%>
    </main>

    <%@ include file="/WEB-INF/segments/footer.jspf"%>

</div>
</body>


</html>
