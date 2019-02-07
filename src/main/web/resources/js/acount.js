$('document').ready(function () {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getPersonInfo",
        contentType: "application/json; charset=utf-8",
    })
        .done(function (data) {
                $('#personInfo').append('<div class="card" style="width: 500px; text-align: center; background-color: #FFF2CD;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '                <div class="card-body">\n' +
                    '                    <h2 class="card-title">' + data.firstName + ' ' + data.lastName + '</h2>\n' +
                    '                    <hr>\n' +
                    '                    <p class="card-text" style="font-size: x-large">Почта: ' + data.username + '<br> Телефон: ' + data.phoneNum + '</p>\n' +
                    '                    <button type="button" class="btn btn-danger" onclick="leaveAccount()" style="margin: 20px; text-align: center">Выйти</button>\n' +
                    '                </div>\n' +
                    '            </div>');
        })
        .fail(function () {
            console.log("Failed");
        });

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getPersonOrders",
        contentType: "application/json; charset=utf-8",
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                $('#personOrders').append('<a href="concreteOrder.jsp" onclick="orderClick(this.innerHTML)" class="list-group-item list-group-item-action flex-column align-items-start" style="background-color: #FFF2CD; text-align: center; margin-top: 10px; width: 550px;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '                    <div class="d-flex w-100 justify-content-between">\n' +
                    '                        <h3 class="mb-1" style="text-align: center">Заказ №' + data[i].Id + ' от ' + data[i].dateCreated + '</h3>\n' +
                    '                    </div>\n' +
                    '                    <hr>\n' +
                    '                    <p style="font-size: x-large" class="mb-1">Тип оплаты: ' + data[i].paymentType + ';<br> Статус оплаты: ' + data[i].paymentStatus + ';<br> Тип доставки: ' + data[i].deliveryType + ';<br> Статус доставки: ' + data[i].deliveryStatus + ';</p>\n' +
                    '                    <small style="font-size: x-large">Дата получения: ' + data[i].dateReceived + ';</small>\n' +
                    '                </a>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })
});

function leaveAccount() {
    window.localStorage.removeItem('token');
    window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/index.jsp";
}

function orderClick(e) {
    var res = e.substring(147);
    res = res.substring(0, res.indexOf(' '));
    window.localStorage.setItem('selectedOrderId', res);
}