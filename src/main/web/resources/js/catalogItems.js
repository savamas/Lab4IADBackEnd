$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    }

    var params = {
        category: window.localStorage.getItem('selectedCategoryId')
    };

    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getFilters",
        data: JSON.stringify(params),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                var filterValuesTmp = data[i].filterValues;
                var str = '<div class="dropdown-menu" aria-labelledby="dropdownMenuButton" style="width: 300px; text-align: center; color: black; background-color: #FFF6F5">\n';
                for (j = 0; j < filterValuesTmp.length; j++) {
                    var str = str + '<div class="form-check">\n' +
                        '  <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="option1">\n' +
                        '  <label class="form-check-label" for="inlineCheckbox1" style="color: black; font-size: large">' + filterValuesTmp[j] + '</label>\n' +
                        '</div>'
                }
                str = str + '</div>';
                $('#listOfFilters').append('<div class="row" style="margin-bottom: 30px;">\n' +
                    '<div class="dropdown">\n' +
                    '  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="width: 300px; background-color: #FFF2CD; color: black; font-style: oblique; font-size: x-large">\n' +
                    data[i].filterName +
                    '  </button>\n' + str +
                '</div>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })

    $.ajax({
        method: "POST",
        url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getItems",
        data: JSON.stringify(params),
        contentType: "application/json; charset=utf-8"
    })
        .done(function (data) {
            for (i = 0; i < data.length; i++) {
                $('#filteredItems').append('<li class="media">\n' +
                    '                    <img class="mr-3" src="' + data[i].imageUrl + '" alt="Generic placeholder image">\n' +
                    '                    <div class="media-body">\n' +
                    '                        <h5 class="mt-0 mb-1">' + data[i].name +  '</h5>\n' +
                    '                    </div>\n' +
                    '                </li>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })

    $( "#filter" ).click(function() {
        alert( "Handler for .click() called." );
    });
});