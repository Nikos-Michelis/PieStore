<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
  <div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
      <h1>The Pies</h1>
      <article>
        <table class="table-pies">
          <caption>Our pies</caption>
          <thead>
            <tr>
              <th></th>
              <th>Pie</th>
              <th>Price/piece</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <c:forEach var="item" items="${requestScope.piesBean}">
                <tr>
                  <td><img src="${item.fileName}" alt="${item.name}"><span>${item.name}</span></td>
                  <td><a href="${pageContext.request.contextPath}/pie?id=${item.id}">${item.name}</a></td>
                  <td>${item.price}€</td>
                </tr>
              </c:forEach>
            </tr>
          </tbody>
        </table>
      </article>

      <%@ include file="/WEB-INF/segments/aside.jspf"%>
    </main>

    <%@ include file="/WEB-INF/segments/footer.jspf"%>
  </div>
</body>
</html>
