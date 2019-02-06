$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    } else {
        document.getElementById('onlyForUnloggedUsers').innerHTML = "<a class=\"nav-link\" href=\"login.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Войти</a>";
    }

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getCartItems",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            if (data == "No Items") {
                $('#itemsInCart').append('<div class="jumbotron" style="background-color: #FFF2CD; width: 600px; margin-top: 20px">\n' +
                    '    <h1 class="display-4">Ваша корзина пуста</h1>\n' +
                    '    <p class="lead">Для совершения покупок перейдите в наш каталог</p>\n' +
                    '    <hr class="my-4">\n' +
                    '    <p style="font-size: x-large"><a href="catalogMain.jsp">Перейти</a></p>\n' +
                    '</div>');
            } else {
                for (i = 0; i < data.length; i++) {
                    if (data[i].date != "") {
                        $('#itemsInCart').append('<li class="media" style="margin-top: 20px; background-color: #FFF2CD; width: 600px; text-align: center">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                            '                    <div class="media-body">\n' +
                            '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                            '                        <hr>\n' +
                            '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                            '                        <p style="font-size: x-large">Дата: ' + data[i].date +  '</p>\n' +
                            // '                        <br>\n' +
                            '        <button type="button" class="btn btn-danger" onclick="deleteItemFromCart(this.parentElement.firstElementChild)">Удалить</button>\n' +
                            '                    </div>\n' +
                            '                </li>');
                    } else {
                        $('#itemsInCart').append('<li class="media" style="margin-top: 20px; background-color: #FFF2CD; width: 600px; text-align: center">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                            '                    <div class="media-body">\n' +
                            '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                            '                        <hr>\n' +
                            '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                            '                        <p style="font-size: x-large">Количество: ' + data[i].amount +  '</p>\n' +
                            // '                        <br>\n' +
                            '        <button type="button" class="btn btn-danger" onclick="deleteItemFromCart(this.parentElement.firstElementChild)">Удалить</button>\n' +
                            '                    </div>\n' +
                            '                </li>');
                    }
                }
                $('#cartConfirmButton').append('<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" style="margin-left: 220px;">\n' +
                    '  Оформить заказ\n' +
                    '</button>\n' +
                    '\n' +
                    '<!-- Modal -->\n' +
                    '<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
                    '  <div class="modal-dialog" role="document">\n' +
                    '    <div class="modal-content">\n' +
                    '      <div class="modal-header">\n' +
                    '        <h5 class="modal-title" id="exampleModalLabel">Подтверждение</h5>\n' +
                    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                    '          <span aria-hidden="true">&times;</span>\n' +
                    '        </button>\n' +
                    '      </div>\n' +
                    '      <div class="modal-body">\n' +
                    '        <label for="deliveryType">Выберите способ доставки:</label>\n' +
                    '            <select id="deliveryType" class="form-control">\n' +
                    '                <option selected>Самовывоз</option>\n' +
                    '                <option>Доставка на дом</option>\n' +
                    '            </select>' +
                    '        <label for="paymentType">Выберите способ оплаты:</label>\n' +
                    '            <select id="paymentType" class="form-control">\n' +
                    '                <option selected>По карте онлайн</option>\n' +
                    '                <option>Наличными при получении</option>\n' +
                    '                <option selected>По карте при получении</option>\n' +
                    '            </select>' +
                    '      </div>\n' +
                    '      <div class="modal-footer">\n' +
                    '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                    '        <button type="button" class="btn btn-primary" onclick="confirmOrder()">Подтвердить</button>\n' +
                    '      </div>\n' +
                    '    </div>\n' +
                    '  </div>\n' +
                    '</div>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })
});

function confirmOrder() {
    var authToken = window.localStorage.getItem('token');
    if (authToken == null) {
        authToken = "Bearer ";
    }
    var dT = $('#deliveryType').val();
    var pT = $('#paymentType').val();
 //   alert(dT);
 //   alert(pT);
    var pS = "";
    if (pT == "По карте онлайн") {
        pS = "Оплачен";
    } else {
        pS = "Не оплачен";
    }
    var myParams = {
        paymentType: pT,
        paymentStatus: pS,
        deliveryType: dT
    };
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/create",
        headers: {
            Authorization: authToken
        },
        data: JSON.stringify(myParams),
        contentType: "application/json",
        error: function(xhr, status, error){
            if (error == "Unauthorized") {
                window.localStorage.removeItem('token');
                window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/login.jsp";
            }
        },
        success: function () {
            $("#cartConfirmButton").empty();
            $('#cartConfirmButton').append('<ul class="list-unstyled" id="itemsInCart"><div class="jumbotron" style="background-color: #FFF2CD; width: 600px; margin-top: 20px">\n' +
                '    <h1 class="display-4">Ваша корзина пуста</h1>\n' +
                '    <p class="lead">Для совершения покупок перейдите в наш каталог</p>\n' +
                '    <hr class="my-4">\n' +
                '    <p style="font-size: x-large"><a href="catalogMain.jsp">Перейти</a></p>\n' +
                '</div></ul>');
            window.location.reload();
        }
    });
}

function deleteItemFromCart(e){
    var j = e.innerHTML;
    var res = j.substring(22);
    res = res.substring(0, res.length - 5);
    var params= {
        name: res
    };
    $.ajax({
        type: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/removeFromCart",
        contentType: "application/json",
        data: JSON.stringify(params),
        async: false,
        success: function () {
            $("#itemsInCart").empty();

            $.ajax({
                method: "GET",
                url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getCartItems",
                contentType: "application/json; charset=utf-8"
            })
                .done(function (data) {
                    if (data == "No Items") {
                        $("#cartConfirmButton").empty();
                        $('#cartConfirmButton').append('<ul class="list-unstyled" id="itemsInCart"><div class="jumbotron" style="background-color: #FFF2CD; width: 600px; margin-top: 20px">\n' +
                            '    <h1 class="display-4">Ваша корзина пуста</h1>\n' +
                            '    <p class="lead">Для совершения покупок перейдите в наш каталог</p>\n' +
                            '    <hr class="my-4">\n' +
                            '    <p style="font-size: x-large"><a href="catalogMain.jsp">Перейти</a></p>\n' +
                            '</div></ul>');
                    } else {
                        $("#cartConfirmButton").empty();
                        $("#cartConfirmButton").append('<ul class="list-unstyled" id="itemsInCart">\n' +
                            '            </ul>');
                        for (i = 0; i < data.length; i++) {
                            if (data[i].date != "") {
                                $('#itemsInCart').append('<li class="media" style="margin-top: 20px; background-color: #FFF2CD; width: 600px; text-align: center">\n' +
                                    '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                                    '                    <div class="media-body">\n' +
                                    '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                                    '                        <hr>\n' +
                                    '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                                    '                        <p style="font-size: x-large">Дата: ' + data[i].date +  '</p>\n' +
                                    // '                        <br>\n' +
                                    '        <button type="button" class="btn btn-danger" onclick="deleteItemFromCart(this.parentElement.firstElementChild)">Удалить</button>\n' +
                                    '                    </div>\n' +
                                    '                </li>');
                            } else {
                                $('#itemsInCart').append('<li class="media" style="margin-top: 20px; background-color: #FFF2CD; width: 600px; text-align: center">\n' +
                                    '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                                    '                    <div class="media-body">\n' +
                                    '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                                    '                        <hr>\n' +
                                    '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                                    '                        <p style="font-size: x-large">Количество: ' + data[i].amount +  '</p>\n' +
                                    // '                        <br>\n' +
                                    '        <button type="button" class="btn btn-danger" onclick="deleteItemFromCart(this.parentElement.firstElementChild)">Удалить</button>\n' +
                                    '                    </div>\n' +
                                    '                </li>');
                            }
                        }
                        $('#cartConfirmButton').append('<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" style="margin-left: 220px;">\n' +
                            '  Оформить заказ\n' +
                            '</button>\n' +
                            '\n' +
                            '<!-- Modal -->\n' +
                            '<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
                            '  <div class="modal-dialog" role="document">\n' +
                            '    <div class="modal-content">\n' +
                            '      <div class="modal-header">\n' +
                            '        <h5 class="modal-title" id="exampleModalLabel">Подтверждение</h5>\n' +
                            '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                            '          <span aria-hidden="true">&times;</span>\n' +
                            '        </button>\n' +
                            '      </div>\n' +
                            '      <div class="modal-body">\n' +
                            '        <label for="deliveryType">Выберите способ доставки:</label>\n' +
                            '            <select id="deliveryType" class="form-control">\n' +
                            '                <option selected>Самовывоз</option>\n' +
                            '                <option>Доставка на дом</option>\n' +
                            '            </select>' +
                            '        <label for="paymentType">Выберите способ оплаты:</label>\n' +
                            '            <select id="paymentType" class="form-control">\n' +
                            '                <option selected>По карте онлайн</option>\n' +
                            '                <option selected>По карте при получении</option>\n' +
                            '                <option>Наличными при получении</option>\n' +
                            '            </select>' +
                            '      </div>\n' +
                            '      <div class="modal-footer">\n' +
                            '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                            '        <button type="button" class="btn btn-primary">Подтвердить</button>\n' +
                            '      </div>\n' +
                            '    </div>\n' +
                            '  </div>\n' +
                            '</div>');
                    }
                })
                .fail(function () {
                    console.log("Failed");
                })
        }
    });
}