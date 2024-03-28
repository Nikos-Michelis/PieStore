<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<%@ include file="/WEB-INF/segments/head.jspf"%>

<body>
  <div class="container">
    <%@ include file="/WEB-INF/segments/header-pages.jspf"%>

    <main>
      <h1>Store</h1>
      <div class="slider">
        <div class="slide slide1">
          <figure>
            <img src="../../images/store1.jpg" alt="Exterior Store Photo">
            <figcaption>Enjoy your pie with a view of Lower Patisia</figcaption>
          </figure>
        </div>
        <div class="slide slide2">
          <figure>
            <img src="../../images/store2.jpg" alt="Exterior Store Photo">
            <figcaption>With plenty of space for your children to play</figcaption>
          </figure>
        </div>
        <div class="slide slide3">
          <figure>
            <img src="../../images/delivery.jpg" alt="Delivery">
            <figcaption>We are happy to make delivery on weekdays (more than 20 pies in the order)</figcaption>
          </figure>
        </div>
        <a class="previous" onclick="plusSlides(-1)">⬅</a>
        <a class="next" onclick="plusSlides(1)">➡</a>
      </div>

      <%@ include file="/WEB-INF/segments/aside.jspf"%>
    </main>

    <%@ include file="/WEB-INF/segments/footer.jspf"%>
  </div>

  <script src="../../js/slider.js"></script>
</body>


</html>
