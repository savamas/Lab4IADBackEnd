$('document').ready(function () {
    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getPersonInfo",
        contentType: "application/json; charset=utf-8",
    })
        .done(function (data) {
                $('#personInfo').append('<div class="card" style="width: 500px; text-align: center">\n' +
                    '                <div class="card-body">\n' +
                    '                    <h5 class="card-title">' + data.firstName + ' ' + data.lastName + '</h5>\n' +
                    '                    <p class="card-text">Почта: ' + data.username + ' Телефон: ' + data.phoneNum + '</p>\n' +
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
                $('#personOrders').append('<a href="concreteOrder.jsp" onclick="orderClick(this.innerHTML)" class="list-group-item list-group-item-action flex-column align-items-start">\n' +
                    '                    <div class="d-flex w-100 justify-content-between">\n' +
                    '                        <h5 class="mb-1">Заказ №' + data[i].Id + ' от ' + data[i].dateCreated + '</h5>\n' +
                    '                    </div>\n' +
                    '                    <p class="mb-1">Тип оплаты: ' + data[i].paymentType + ' Статус оплаты: ' + data[i].paymentStatus + ' Тип доставки: ' + data[i].deliveryType + ' Статус доставки: ' + data[i].deliveryStatus + '</p>\n' +
                    '                    <small>Дата получения: ' + data[i].dateReceived + '</small>\n' +
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
    var res = e.substring(120);
    res = res.substring(0, res.indexOf(' '));
    window.localStorage.setItem('selectedOrderId', res);
}