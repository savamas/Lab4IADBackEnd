<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдержка</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="https://momentjs.com/downloads/moment-with-locales.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/js/tempusdominus-bootstrap-4.min.js"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/tempusdominus-bootstrap-4/5.0.0-alpha14/css/tempusdominus-bootstrap-4.min.css" />
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.6.3/css/all.css" integrity="sha384-UHRtZLI+pbxtHCWp1t77Bi1L4ZtiqrqD80Kn4Z8NTSRyMA2Fd33n5dQ8lWUE00s/" crossorigin="anonymous">
    <%--<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">--%>
    <%--<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>--%>
    <%--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>--%>
    <%--<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>--%>
    <script type="text/javascript" src="resources/js/backOffice.js"></script>
</head>
<body style="background-color: #FFF6F5">

<nav class="navbar fixed-top navbar-expand navbar-light" style="background-color: #AF734E; font-size: x-large">
    <img src="Logo.svg" width="50" height="50" class="d-inline-block align-top" alt="zz">
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a id="leaveCourier" class="nav-link" href="index.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Выйти</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="backOffice.jsp" style="color: #FFF2CD; font-family: Rockwell; font-size: 25px;">Все заказы</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="activeOrders.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Ваши заказы</a>
            </li>
        </ul>
    </div>
</nav>

<div class="container" id="wholePage">
    <div class="row">
        <div class="col-sm" style="margin-top: 90px">
            <div class="list-group" id="unclaimedOrders">
            </div>
        </div>
        <div  id="orderPicker" class="col-sm" style="margin-top: 100px;">
            <div id="orderPicker2" style="position:fixed; background-color: #FFF2CD; height: 180px;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); padding: 10px">
                <label for="orderToProcess"><h3>Введите номер заказа на обработку:</h3></label>
                <input type="number" class="form-control" id="orderToProcess" placeholder="Номер заказа" required min="1">
                <!-- Button trigger modal -->
                <button type="button" id="confirmOrderToProcess" class="btn btn-primary" data-toggle="modal" style="margin-top: 10px; margin-bottom: 50px">
                    Забрать себе
                </button>
            </div>
        </div>
    </div>
</div>

</body>
</html>