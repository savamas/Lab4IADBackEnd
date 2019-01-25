<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Выдержка</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css" integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" crossorigin="anonymous"></script>
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
    <script type="text/javascript" src="resources/js/index.js"></script>
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
            <li class="nav-item">
                <a class="nav-link" href="login.jsp" style="color: #F3ECD6; font-family: Rockwell; font-size: 25px;">Войти</a>
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

<div class="media">
    <img class="mr-3" src="d34f9b172e3bc0171ace8b1ae2150763.jpg" alt="Generic placeholder image" width="600" style="margin: 20px">
    <div class="media-body" style="margin: 20px">
        <h1 class="mt-0">Выдержка</h1>
        <p style="font-size: x-large; font-style: oblique">Один из главных флагманских магазинов по продаже и предоставлению в аренду фото/видео оборудования, а также аренды локаций. Именно здесь вы сможете выбрать понравившийся вам пейзаж и запечалеть долгожданный момент!</p>
    </div>
</div>

<div class="media">
    <div class="media-body" style="margin: 20px">
        <h1 class="mt-0 mb-1">Только у нас!</h1>
        <p style="font-size: x-large; font-style: oblique"> Постоянные акции и спецпредложения, от которых вы не сможете отказаться. Штативы, подставки, звуковое и операторское оборудование прямиком из Германии!</p>
    </div>
    <img class="ml-3" src="027981ec990f556d8370a14e3e774061_L.jpg" alt="Generic placeholder image" style="margin: 20px">
</div>

<div class="media">
    <img class="mr-3" src="semejnye-traditsii-tsennosti.jpg" alt="Generic placeholder image" style="margin: 20px">
    <div class="media-body" style="margin: 20px">
        <h1 class="mt-0">Лучшие локации в России!</h1>
        <p style="font-size: x-large; font-style: oblique">Работают в нашей компании исключительно профессионалы, мастера своего дела. Они не остановятся пока вы не будете полностью довольны своим фото!</p>
    </div>
</div>

</body>
</html>
