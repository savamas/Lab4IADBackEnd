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
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getActiveOrders",
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
            for (i = 0; i < data.length; i++) {
                dataToSave[i] = data[i];
                $('#activeOrders').append('<div class="list-group-item list-group-item-action flex-column align-items-start" style="background-color: #FFF2CD; text-align: center; margin-top: 10px;">\n' +
                    '                    <div class="d-flex w-100 justify-content-between">\n' +
                    '                        <h3 class="mb-1" style="text-align: center">Заказ №' + data[i].Id + ' от ' + data[i].dateCreated + '</h3>\n' +
                    '                    </div>\n' +
                    '                    <hr>\n' +
                    '                    <p style="font-size: x-large" class="mb-1">Тип оплаты: ' + data[i].paymentType + ';<br> Статус оплаты: ' + data[i].paymentStatus + ';<br> Тип доставки: ' + data[i].deliveryType + ';<br> Статус доставки: ' + data[i].deliveryStatus + ';</p>\n' +
                    '                </div>');
            }
        }
    });

    $('#changeInit').click(function () {
        var help = $('#orderToChange').val();
        isGood = false;
        
        for (i = 0; i < dataToSave.length; i++) {
            if (help == dataToSave[i].Id) {
                isGood = true;
            }
        }

        if (isGood == true) {
            var params = {
                id: help
            };

            $.ajax({
                method: "POST",
                url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getOrder",
                headers: {
                    Authorization: authToken
                },
                data: JSON.stringify(params),
                contentType: "application/json; charset=utf-8",
                error: function(xhr, status, error){
                    if (error == "Unauthorized") {
                        window.localStorage.removeItem('token');
                        window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/login.jsp";
                    }
                },
                success: function (data) {
                    var str1 = "";
                    var str2 = "";
                    if (data.paymentStatus == "Оплачен") {
                        str1 = '                            <select id="payment" class="form-control">\n' +
                            '                                <option selected>Оплачен</option>\n' +
                            '                                <option>Не оплачен</option>\n' +
                            '                            </select>\n';
                    } else {
                        str1 = '                            <select id="payment" class="form-control">\n' +
                            '                                <option>Оплачен</option>\n' +
                            '                                <option selected>Не оплачен</option>\n' +
                            '                            </select>\n';
                    }
                    if (data.deliveryStatus == "В обработке") {
                        str2 = '                            <select id="delivery" class="form-control">\n' +
                            '                                <option selected>В обработке</option>\n' +
                            '                                <option>Собирается</option>\n' +
                            '                                <option>Готово к доставке</option>\n' +
                            '                                <option>Готово к выдаче</option>\n' +
                            '                                <option>Завершено</option>\n' +
                            '                            </select>\n';
                    } else {
                        if (data.deliveryStatus == "Собирается") {
                            str2 = '                            <select id="delivery" class="form-control">\n' +
                                '                                <option>В обработке</option>\n' +
                                '                                <option selected>Собирается</option>\n' +
                                '                                <option>Готово к доставке</option>\n' +
                                '                                <option>Готово к выдаче</option>\n' +
                                '                                <option>Завершено</option>\n' +
                                '                            </select>\n';
                        } else {
                            if (data.deliveryStatus == "Готово к доставке") {
                                str2 = '                            <select id="delivery" class="form-control">\n' +
                                    '                                <option>В обработке</option>\n' +
                                    '                                <option>Собирается</option>\n' +
                                    '                                <option selected>Готово к доставке</option>\n' +
                                    '                                <option>Готово к выдаче</option>\n' +
                                    '                                <option>Завершено</option>\n' +
                                    '                            </select>\n';
                            } else {
                                if (data.deliveryStatus == "Готово к выдаче") {
                                    str2 = '                            <select id="delivery" class="form-control">\n' +
                                        '                                <option>В обработке</option>\n' +
                                        '                                <option>Собирается</option>\n' +
                                        '                                <option>Готово к доставке</option>\n' +
                                        '                                <option selected>Готово к выдаче</option>\n' +
                                        '                                <option>Завершено</option>\n' +
                                        '                            </select>\n';
                                } else {
                                    str2 = '                            <select id="delivery" class="form-control">\n' +
                                        '                                <option>В обработке</option>\n' +
                                        '                                <option>Собирается</option>\n' +
                                        '                                <option>Готово к доставке</option>\n' +
                                        '                                <option>Готово к выдаче</option>\n' +
                                        '                                <option selected>Завершено</option>\n' +
                                        '                            </select>\n';
                                }
                            }
                        }
                    }
                    //alert("EBANAT");
                    $('#exampleModal').remove();

                    $('#orderToPick').append('<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true"><div class="modal-dialog" role="document">\n' +
                        '                    <div class="modal-content">\n' +
                        '                        <div class="modal-header">\n' +
                        '                            <h5 class="modal-title" id="exampleModalLabel">Внесите изменения:</h5>\n' +
                        '                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                        '                                <span aria-hidden="true">&times;</span>\n' +
                        '                            </button>\n' +
                        '                        </div>\n' +
                        '                        <div class="modal-body">\n' +
                        '                            <label for="payment">Статус оплаты</label>\n' + str1 +
                        '                            <label for="delivery">Статус доставки</label>\n' + str2 +
                        '                        </div>\n' +
                        '                        <div class="modal-footer">\n' +
                        '                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                        '                            <button type="button" class="btn btn-primary" onclick="confirmChange()">Подтвердить</button>\n' +
                        '                        </div>\n' +
                        '                    </div>\n' +
                        '                </div></div>');
                }
            });
        } else {
            $("#orderToPick").append("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                "  Неправильно выбран номер заказа!\n" +
                "</div>");
        }
    });
});

function confirmChange() {
    var res = $('#orderToChange').val();

    var myToken = window.localStorage.getItem('token');
    if (myToken == null) {
        myToken = "Bearer ";
    }

    var t1 = $('#payment').val();
    var t2 = $('#delivery').val();

    var changeOrder = {
        orderId: res,
        newDeliveryStatus: t2,
        newPaymentStatus: t1
    };
    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/setStatus",
        headers: {
            Authorization: myToken
        },
        data: JSON.stringify(changeOrder),
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