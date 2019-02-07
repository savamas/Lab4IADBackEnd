$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    } else {
        document.getElementById('onlyForUnloggedUsers').innerHTML = "<a class=\"nav-link\" href=\"login.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Войти</a>";
    }

    var params = {
      id: window.localStorage.getItem('selectedOrderId')
    };
    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/getOrderItems",
        data: JSON.stringify(params),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                    if (data[i].date != "") {
                        $('#itemsInOrder').append('<li class="media" style="margin-top: 20px; width: 750px; text-align: center">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                            '                    <div class="media-body" style="height: 240px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); background-color: #FFF2CD;">\n' +
                            '                        <h2 class="mt-0 mb-1">' + data[i].name +  '</h2>\n' +
                            '                        <hr>\n' +
                            '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '\n' +
                            '                        <br>Дата: ' + data[i].date +  '</p>\n' +
                            '                    </div>\n' +
                            '                </li>');
                    } else {
                        $('#itemsInOrder').append('<li class="media" style="margin-top: 20px; background-color: #FFF2CD; width: 750px; text-align: center">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image" style="width: 300px; height: 240px; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                            '                    <div class="media-body">\n' +
                            '                        <h2 class="mt-0 mb-1">' + data[i].name +  '</h2>\n' +
                            '                        <hr>\n' +
                            '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '\n' +
                            '                        <br>Количество: ' + data[i].amount +  '</p>\n' +
                            '                    </div>\n' +
                            '                </li>');
                    }
                }
        })
        .fail(function () {
            console.log("Failed");
        })
});