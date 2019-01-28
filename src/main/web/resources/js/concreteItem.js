$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    }

    var params = {
        category: window.localStorage.getItem('selectedCategoryId'),
        id: window.localStorage.getItem('selectedItemId')
    };

    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getItemById",
        data: JSON.stringify(params),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                $('#concreteItemShow').append('<div class="jumbotron">\n' +
                    '        <h1 class="display-4">Hello, world!</h1>\n' +
                    '        <p class="lead">This is a simple hero unit, a simple jumbotron-style component for calling extra attention to featured content or information.</p>\n' +
                    '        <hr class="my-4">\n' +
                    '        <p>It uses utility classes for typography and spacing to space content out within the larger container.</p>\n' +
                    '        <a class="btn btn-primary btn-lg" href="#" role="button">Learn more</a>\n' +
                    '    </div>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })
});