var dataTmp = [];

$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    } else {
        document.getElementById('onlyForUnloggedUsers').innerHTML = "<a class=\"nav-link\" href=\"login.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Войти</a>";
    }

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getCategories",
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                dataTmp[i] = data[i];
                $('#listOfCategories').append('<div class="card" style="width: 18rem; height: 180px; margin-bottom: 220px">\n' +
                    '    <img class="card-img-top" src="' + data[i].imageUrl + '" alt="Card image cap" style="height: 300px; object-fit: cover; border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '    <div class="card-body" style="text-align: center; background-color: #FFF2CD; font-size: xx-large; font-style: oblique">\n' +
                    '      <a href="catalogItems.jsp" onClick="linkClicked(this.innerHTML)"><h2 class="card-title">' + data[i].name + '</h2></a>\n' +
                    '    </div>\n' +
                    '  </div>')
            }
        })
        .fail(function () {
            console.log("Failed 4");
        })
});

function linkClicked(e) {
    var res = e.substring(23);
    res = res.substring(0, res.length - 5);
    for (i = 0; i < dataTmp.length; i++) {
        if (dataTmp[i].name == res) {
            window.localStorage.setItem('selectedCategoryId', dataTmp[i].Id);
        }
    }
}