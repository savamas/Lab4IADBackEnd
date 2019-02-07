$('document').ready(function () {
    bum = [];
    dateH = new Date();

    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    } else {
        document.getElementById('onlyForUnloggedUsers').innerHTML = "<a class=\"nav-link\" href=\"login.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Войти</a>";
    }

    var params = {
        id: window.localStorage.getItem('selectedItemId')
    };

    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getItemById",
        data: JSON.stringify(params),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            var calendar = '';
            var str = '';
            if (window.localStorage.getItem('selectedCategoryId') == 2) {

                $.ajax({
                    method: "GET",
                    url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getBookedDates",
                    contentType: "application/json; charset=utf-8",
                    async: false,
                    success: function (bookedDates) {
                        for (i = 0; i < bookedDates.length; i++){
                            bum[i] = moment(bookedDates[i]);
                            dateH = new Date();
                            dateH.setDate(dateH.getDate()-1);
                          //  alert(bookedDates[i]);
                        }

                        calendar = '<div class="container">\n' +
                            '    <div class="row">\n' +
                            '        <div class="col-sm-6">\n' +
                            '            <div class="form-group">\n' +
                            '                <div class="input-group date" id="datetimepicker4" data-target-input="nearest">\n' +
                            '                    <input type="text" class="form-control datetimepicker-input" data-target="#datetimepicker4" id="bookingTime"/>\n' +
                            '                    <div class="input-group-append" data-target="#datetimepicker4" data-toggle="datetimepicker">\n' +
                            '                        <div class="input-group-text"><i class="fa fa-calendar"></i></div>\n' +
                            '                    </div>\n' +
                            '                </div>\n' +
                            '            </div>\n' +
                            '        </div>\n' +
                            '        <script type="text/javascript">\n' +
                            '            $(function () {\n' +
                            // '                var bum = [];\n' +
                            // '                for (i = 0; i < bookedDates.length; i++){\n' +
                            // '                    bum[i] = moment(bookedDates[i]);\n' +
                            // '                }\n' +
                            '                $(\'#datetimepicker4\').datetimepicker({\n' +
                            '                    format: \'L\',\n' +
                            '                    disabledDates: bum, minDate: dateH\n' +
                            '                });\n' +
                            '            });\n' +
                            '        </script>\n' +
                            '    </div>\n' +
                            '</div>';
                     //   alert("EBLAN");
                    }
                }).fail(function () {
                        console.log("Failed 4");
                    })

                //alert("TUPOI");
                str = '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '  Добавить в корзину\n' +
                    '</button>\n' +
                    '\n' +
                    '<div class="modal fade bd-example-modal-lg" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
                    '  <div class="modal-dialog modal-lg" role="document">\n' +
                    '    <div class="modal-content">\n' +
                    '      <div class="modal-header">\n' +
                    '        <h5 class="modal-title" id="exampleModalLabel">Выберите дату и время бронирования</h5>\n' +
                    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                    '          <span aria-hidden="true">&times;</span>\n' +
                    '        </button>\n' +
                    '      </div>\n' +
                    '      <div class="modal-body">\n' + calendar +
                    '      </div>\n' +
                    '      <div class="modal-footer">\n' +
                    '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                    '        <button type="button" class="btn btn-primary" onclick="addToCart()">Добавить</button>\n' +
                    '      </div>\n' +
                    '    </div>\n' +
                    '  </div>\n' +
                    '</div>';
            } else {
                str = '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal" style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '  Добавить в корзину\n' +
                    '</button>\n' +
                    '\n' +
                    '<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
                    '  <div class="modal-dialog" role="document">\n' +
                    '    <div class="modal-content">\n' +
                    '      <div class="modal-header">\n' +
                    '        <h5 class="modal-title" id="exampleModalLabel">Выберите количество</h5>\n' +
                    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                    '          <span aria-hidden="true">&times;</span>\n' +
                    '        </button>\n' +
                    '      </div>\n' +
                    '      <div class="modal-body">\n' +
                    '        <input type="number" class="form-control" id="quantity" placeholder="Количество" required min="1" max="10">\n' +
                    '      </div>\n' +
                    '      <div class="modal-footer">\n' +
                    '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                    '        <button type="button" class="btn btn-primary" onclick="addToCart()">Добавить</button>\n' +
                    '      </div>\n' +
                    '    </div>\n' +
                    '  </div>\n' +
                    '</div>';
            }
            $('#concreteItemShow').append('<div class="card" style="width: 27rem; text-align: center; margin: auto;">\n' +
                '    <img class="card-img-top" src="' + data.url + '" alt="Card image cap" width="320px" style=" border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                '  <div class="card-body" style=" background-color: #FFF2CD; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); margin-top: 8px">\n' +
                '        <h1 class="display-4">' + data.name + '</h1>\n' +
                '        <hr class="my-4">\n' +
                '        <p style="font-size: x-large;">Цена: ' + data.price + '</p>\n' +
                '                        <p style="font-size: x-large">Описание: ' + data.description +  '</p>\n' + str +
                '  </div>\n' +
                '</div>');
        })
        .fail(function () {
            console.log("Failed");
        })

});

function addToCart() {
    var a;
    if (window.localStorage.getItem("selectedCategoryId") == 2) {
        a = $("#bookingTime").val();

        if (a == "") {
            a = "01/28/2019";
        }

        var sendTime = {
            id : window.localStorage.getItem("selectedItemId"),
            amount: "1",
            booking: a
        };

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/addToCart",
            contentType: "application/json",
            data: JSON.stringify(sendTime)
        });
    } else {

        a = $("#quantity").val();

        if (a == "") {
            a = "1";
        }

        var sendQuantity = {
            id : window.localStorage.getItem("selectedItemId"),
            amount : a,
            booking: ""
        };
        $.ajax({
            type: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/order/addToCart",
           contentType: "application/json",
           data: JSON.stringify(sendQuantity)
        });
    }
    window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/catalogItems.jsp";
}