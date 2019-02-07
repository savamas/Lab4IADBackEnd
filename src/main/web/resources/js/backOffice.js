var dataToSave = [];

$('document').ready(function () {
    $("#leaveCourier").click(function () {
        window.localStorage.removeItem('token');
    });


    var authToken = window.localStorage.getItem('token');
    if (authToken == null) {
        authToken = "Bearer ";
    }

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getUnclaimedOrders",
        headers: {
            Authorization: authToken
        },
        contentType: "application/json; charset=utf-8",
        error: function(xhr, status, error){
            if (error == "Unauthorized") {
                window.localStorage.removeItem('token');
                window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/login.jsp";
            }
        },
        success: function (data) {
            dataToSave = [];
            if (data.length != 0) {
                for (i = 0; i < data.length; i++) {
                    dataToSave[i] = data[i];
                    $('#unclaimedOrders').append('<div class="list-group-item list-group-item-action flex-column align-items-start" style="background-color: #FFF2CD; text-align: center; margin-top: 10px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                        '                    <div class="d-flex w-100 justify-content-between">\n' +
                        '                        <h3 class="mb-1" style="text-align: center">Заказ №' + data[i].Id + ' от ' + data[i].dateCreated + '</h3>\n' +
                        '                    </div>\n' +
                        '                    <hr>\n' +
                        '                    <p style="font-size: x-large" class="mb-1">Тип оплаты: ' + data[i].paymentType + ';<br> Статус оплаты: ' + data[i].paymentStatus + ';<br> Тип доставки: ' + data[i].deliveryType + ';<br> Статус доставки: ' + data[i].deliveryStatus + ';</p>\n' +
                        '                </div>');
                }
            } else {
                $("#wholePage").empty();
                $("#wholePage").append('<div class="jumbotron" style="background-color: #FFF2CD; margin-top: 20px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '  <h1 class="display-4">Ожидайте!</h1>\n' +
                    '  <p class="lead" style="font-size: x-large">В данный момент заказов на обработку не поступало.</p>\n' +
                    '  <hr class="my-4">\n' +
                    '  <p>Не выходите из режима администратора для своевременной получения заказа.</p>\n' +
                    '</div>');
            }
        }
    });

    $('#confirmOrderToProcess').click(function () {
        var res = $('#orderToProcess').val();
        var tmp = 0;

        for (i = 0; i < dataToSave.length; i++) {
            tmp++;
            if (dataToSave[i].Id == res) {
                tmp--;
                var myToken = window.localStorage.getItem('token');
                if (myToken == null) {
                    myToken = "Bearer ";
                }

                var idOrder = {
                    id: res
                };

                $.ajax({
                    method: "POST",
                    url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/claimOrder",
                    headers: {
                        Authorization: myToken
                    },
                    data: JSON.stringify(idOrder),
                    contentType: "application/json; charset=utf-8",
                    error: function(xhr, status, error){
                        if (error == "Unauthorized") {
                            window.localStorage.removeItem('token');
                            window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/login.jsp";
                        }
                    },
                    success: function () {
                        window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/activeOrders.jsp";
                    }
                });
            }
        }

        if (tmp == dataToSave.length) {
            $("#orderPicker").append("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                "  Неправильно выбран номер заказа!\n" +
                "</div>");
        }
    })
});