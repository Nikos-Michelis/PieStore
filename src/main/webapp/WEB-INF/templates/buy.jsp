<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="formOrder" class="com.nick.jakartaproject.form.FormOrder" scope="request"/>
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
<div class="container">
  <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

  <main>
    <h1>The Pies</h1>
    <article>
      <h2>Our pies at your place! </h2>
      <p>
        Ordering hours (18:00-22:00)
      </p>
      <c:choose>
        <c:when test="${requestScope['success']}">
          <div class="success-message">
            We have received your order and our pies will be with you soon!
          </div>
        </c:when>
        <c:otherwise>

          <c:choose>
            <c:when test="${user.id != null}">
              <p>
                Welcome ${user.fullName}! You can repeat some of your previous orders:
              </p>
              <ul>
                <c:set var="cnt" value="0" scope="request"/>
                <c:forEach var="order" items="${requestScope['previousOrders']}">
                  <li>
                    <a href="buy?previousorder=${cnt}">${order.stamp}: ${order.orderItems}
                    </a>
                    <c:set var="cnt" value="${cnt + 1}" scope="request"/>
                  </li>
                </c:forEach>
              </ul>
            </c:when>
            <c:otherwise>
            </c:otherwise>
          </c:choose>

          <c:if test="${requestScope['errors']!=null}">
            <div class="error-message">${requestScope['errors']} </div>
          </c:if>
          <form action="buy" method="POST" class="form buy">
            <section class="customer-info">
              <h3>
                Delivery Information:
              </h3>
              <label for="txtName">FullName(*):</label>
              <input type="text" id="txtName" name="name" minlength="3" maxlength="40" size="40" required
                     value="${empty formOrder.fullName? user.fullName : formOrder.fullName}">
              <label for="txtAddress">Address(Street - number)(*):</label>
              <input type="text" id="txtAddress" name="address" minlength="5" maxlength="40" size="40" required
                     value="${empty formOrder.address? formOrder.address : formOrder.address}">
              <label for="slArea"> Area(*):</label>
              <select id="slArea" name="area" required>
                <c:forEach var="item" items="${requestScope['areas']}">
                  <option value="${item.id}" ${formOrder.areaId == item.id?"selected":""}>${item.description}</option>
                </c:forEach>
              </select>
              <label for="txtEmail">E-mail(*): </label>
              <input type="email" id="txtEmail" name="email" size="40" required
                     value="${empty formOrder.email? user.email : formOrder.email}">
              <label for="txtTel">phone(*): </label>
              <input type="tel" id="txtTel" name="tel" size="15" required
                     value="${empty formOrder.tel? user.tel : formOrder.tel}">
              <label for="txtMessage">Special Delivery Information: </label>
              <textarea id="txtMessage" name="message" rows="6" cols="40">${formOrder.comments}</textarea>
              <span>(*) The fields are mandatory</span>
            </section>

            <section class="order">
              <h3>
                Order:
              </h3>
              <c:forEach var="pie" items="${requestScope['pies']}">
                <label for="txt${pie.name}">${pie.name} </label>
                <input type="number" id="txt${pie.name}" name="Order${pie.name}" min="0" max="100"
                       value="${empty formOrder.orderItems?
                                 (empty requestScope[pie.name]? 0: requestScope[pie.name]):formOrder.orderItems[pie.id-1].quantity}">
              </c:forEach>

              <input type="checkbox" id="chkΟffer1" name="offer1" value="offer1-on" ${formOrder.offer==true?"checked":""}>
              <label for="chkΟffer1">Incorporation of offer on 10 or 1 gift pie</label>
            </section>

            <section class="payment">
              <h3>
                Method of Payment:
              </h3>
              <div class="radio-buttons">
                <input type="radio" id="rdVISA" name="payment" value="visa" ${formOrder.payment=='visa'?"checked":""}>
                <label for="rdVISA">VISA</label>
                <input type="radio" id="rdCash" name="payment" value="cash" ${formOrder.payment=='cash'?"checked":""}>
                <label for="rdCash">Cash</label>
              </div>
            </section>
            <div class="buttons">
              <input type="submit" value="Send">
              <input type="reset" value="Clear">
            </div>
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
