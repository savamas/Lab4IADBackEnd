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
                $('#activeOrders').append('<div class="list-group-item list-group-item-action flex-column align-items-start" style="background-color: #FFF2CD; text-align: center; margin-top: 10px;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
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
            confirmChange();
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