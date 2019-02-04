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
            for (i = 0; i < data.length; i++) {
                $('#activeOrders').append('<div class="list-group-item list-group-item-action flex-column align-items-start">\n' +
                    '                    <div class="d-flex w-100 justify-content-between">\n' +
                    '                        <h5 class="mb-1">Заказ №' + data[i].Id + ' от ' + data[i].dateCreated + '</h5>\n' +
                    '                    </div>\n' +
                    '                    <p class="mb-1">Тип оплаты: ' + data[i].paymentType + ' Статус оплаты: ' + data[i].paymentStatus + ' Тип доставки: ' + data[i].deliveryType + ' Статус доставки: ' + data[i].deliveryStatus + '</p>\n' +
                    '                    <small>Дата получения: ' + data[i].dateReceived + '</small>\n' +
                    '</div>');
            }
        }
    });

    $('#confirmChange').click(function () {
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
    })
});