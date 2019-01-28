$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
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
            var str = '';
            if (window.localStorage.getItem('selectedCategoryId') == 2) {
                str = '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">\n' +
                    '  Добавить в корзину\n' +
                    '</button>\n' +
                    '\n' +
                    '<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">\n' +
                    '  <div class="modal-dialog" role="document">\n' +
                    '    <div class="modal-content">\n' +
                    '      <div class="modal-header">\n' +
                    '        <h5 class="modal-title" id="exampleModalLabel">Выберите дату и время бронирования</h5>\n' +
                    '        <button type="button" class="close" data-dismiss="modal" aria-label="Close">\n' +
                    '          <span aria-hidden="true">&times;</span>\n' +
                    '        </button>\n' +
                    '      </div>\n' +
                    '      <div class="modal-body">\n' +
                    '        Here will be calendar\n' +
                    '      </div>\n' +
                    '      <div class="modal-footer">\n' +
                    '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                    '        <button type="button" class="btn btn-primary">Добавить</button>\n' +
                    '      </div>\n' +
                    '    </div>\n' +
                    '  </div>\n' +
                    '</div>';
            } else {
                str = '<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#exampleModal">\n' +
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
                    '        Here will quantity picker\n' +
                    '      </div>\n' +
                    '      <div class="modal-footer">\n' +
                    '        <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>\n' +
                    '        <button type="button" class="btn btn-primary">Добавить</button>\n' +
                    '      </div>\n' +
                    '    </div>\n' +
                    '  </div>\n' +
                    '</div>';
            }
            $('#concreteItemShow').append('<div class="jumbotron" style="background-color: #FFF2CD">\n' +
                '        <h1 class="display-4">' + data.name + '</h1>\n' +
                '        <hr class="my-4">\n' +
                '    <img class="card-img-top" src="' + data.imageUrl + '" alt="Card image cap" width="300px" height="300">\n' +
                '        <p>' + data.price + '</p>\n' +
                '        <p>Here will be definition!</p>\n' + str +
                '    </div>');
        })
        .fail(function () {
            console.log("Failed");
        })
});