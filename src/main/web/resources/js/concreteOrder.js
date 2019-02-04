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
                        $('#itemsInOrder').append('<li class="media">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image">\n' +
                            '                    <div class="media-body">\n' +
                            '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h3 class="mt-0 mb-1">' + data[i].name +  '</h3></a>\n' +
                            '                        Стоимость: ' + data[i].price +  '\n' +
                            '                        Дата: ' + data[i].date +  '\n' +
                            '                    </div>\n' +
                            '                </li>');
                    } else {
                        $('#itemsInOrder').append('<li class="media">\n' +
                            '                    <img class="mr-3" src="' + data[i].url + '" alt="Generic placeholder image">\n' +
                            '                    <div class="media-body">\n' +
                            '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h3 class="mt-0 mb-1">' + data[i].name +  '</h3></a>\n' +
                            '                        Стоимость: ' + data[i].price +  '\n' +
                            '                        Количество: ' + data[i].amount +  '\n' +
                            '                    </div>\n' +
                            '                </li>');
                    }
                }
        })
        .fail(function () {
            console.log("Failed");
        })
});