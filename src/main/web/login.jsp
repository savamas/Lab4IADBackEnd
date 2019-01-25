<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдержка</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <%--<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>--%>
    <%--<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>--%>
    <%--<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>--%>
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="resources/js/login.js"></script>
</head>
<body style="background-color: #FFF6F5">

<nav class="navbar navbar-expand navbar-light" style="background-color: #AF734E; font-size: x-large">
    <a class="navbar-brand" href="index.jsp">Выдержка</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="catalogMain.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Каталог</a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="login.jsp" style="color: #FFF2CD; font-family: Rockwell; font-size: 25px;">Войти</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="about.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Справка</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="cart.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Корзина</a>
            </li>
            <li class="nav-item" id="onlyForLoggedUsers">
                <%--<a class="nav-link" href="cart.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Корзина</a>--%>
            </li>
        </ul>
    </div>
</nav>

<div id="loginAlert"></div>

<form style="margin: 20px" id="loginFormSubmit">
    <div class="form-group">
        <label for="userEmail">Email адрес</label>
        <input type="email" class="form-control" id="userEmail" aria-describedby="emailHelp" placeholder="Введите email" required>
        <small id="emailHelp" class="form-text text-muted">Вся информация о пользователях конфиденциальна.</small>
    </div>
    <div class="form-group">
        <label for="userPassword">Пароль</label>
        <input type="password" class="form-control" id="userPassword" placeholder="Пароль" minlength="6" required>
    </div>
    <div><a href="passwordRecovery.jsp">Забыли пароль?</a></div>
    <div><a href="register.jsp">Нет аккаунта? Зарегестрироваться!</a></div>
    <button type="submit" class="btn btn-primary">Войти</button>
</form>

</body>
</html>
