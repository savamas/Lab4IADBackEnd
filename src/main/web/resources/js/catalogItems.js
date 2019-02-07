var filtersMain = '"filters":[';
var filterParams = [];
var dataToSave = [];

$('document').ready(function () {
    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    } else {
        document.getElementById('onlyForUnloggedUsers').innerHTML = "<a class=\"nav-link\" href=\"login.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Войти</a>";
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
                var str = '<div class="dropdown-menu" aria-labelledby="dropdownMenuButton" style="width: 500px; text-align: center; color: black; background-color: #FFF6F5">\n';
                for (j = 0; j < filterValuesTmp.length; j++) {
                    var str = str + '<div class="form-check">\n' +
                        '  <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="' + data[i].filterName + ' ' + filterValuesTmp[j] + '" onclick="test(this)">\n' +
                        '  <label class="form-check-label" for="inlineCheckbox1" style="color: black; font-size: large">' + filterValuesTmp[j] + '</label>\n' +
                        '</div>'
                }
                str = str + '</div>';
                $('#listOfFilters').append('<div class="row" style="margin-bottom: 30px;">\n' +
                    '<div class="dropdown">\n' +
                    '  <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" style="width: 500px; background-color: #FFF2CD; color: black; font-size: xx-large; font-weight: normal;box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    data[i].filterName +
                    '  </button>\n' + str +
                '</div>');
                filtersMain = filtersMain + '{"filterName":"' + data[i].filterName + '","filterValues":[]},';
            }
            filtersMain = filtersMain.substring(0, filtersMain.length - 1);
            filtersMain = filtersMain + "]";
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
            dataToSave = [];
            for (i = 0; i < data.length; i++) {
                dataToSave[i] = data[i];
                $('#filteredItems').append('<li class="media list-inline-item" style="margin-top: 10px; text-align: center; width: 600px">\n' +
                    '                    <img class="mr-3" src="' + data[i].url + '" width="250px" height="220px" alt="Generic placeholder image" style=" border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                    '                    <div class="media-body" style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); background-color: #FFF2CD; height: 220px;">\n' +
                    '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                    '                        <hr>\n' +
                    '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                    '                    </div>\n' +
                    '                </li>');
            }
        })
        .fail(function () {
            console.log("Failed");
        })

    $("#search").click(function () {
        $("#filteredItems").empty();
        var queryTmp = $("#searchInput").val();
        var sendQuery = {
            category: window.localStorage.getItem('selectedCategoryId'),
            query: queryTmp
        };

        $.ajax({
            method: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getSearchedItems",
            data: JSON.stringify(sendQuery),
            contentType: "application/json; charset=utf-8"
        })
            .done(function (data) {
                dataToSave = [];
                for (i = 0; i < data.length; i++) {
                    dataToSave[i] = data[i];
                    $('#filteredItems').append('<li class="media" style="margin-top: 10px; text-align: center; width: 600px">\n' +
                        '                    <img class="mr-3" src="' + data[i].url + '" width="250px" height="220px" alt="Generic placeholder image" style=" border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                        '                    <div class="media-body" style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); background-color: #FFF2CD; height: 220px;">\n' +
                        '                        <a href="concreteItem.jsp"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                        '                        <hr>\n' +
                        '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                        '                    </div>\n' +
                        '                </li>');
                }
            })
            .fail(function () {
                console.log("Failed");
            })
    });

    $( "#filter" ).click(function() {
        $("#filteredItems").empty();

        var sendData = '{"category":"' + window.localStorage.getItem('selectedCategoryId') + '",' + filtersMain + '}';

        var ob = JSON.parse(sendData);

        var tmpFilters = ob.filters;

        var count;

        do {
            count = 0;
            for (i = 0; i < tmpFilters.length; i++) {
                if (tmpFilters[i].filterValues.length == 0) {
                    count++;
                    tmpFilters.splice(i, 1);
                }
            }
        } while(count != 0);

        for (i = 0; i < tmpFilters.length; i++) {
            if (tmpFilters[i].filterValues.length == 0) {
                tmpFilters.splice(i, 1);
            }
        }

        ob.filters = tmpFilters;

        sendData = JSON.stringify(ob);

        $.ajax({
            method: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/catalogue/getFilteredItems",
            data: sendData,
            contentType: "application/json; charset=utf-8"
        })
            .done(function (data) {
                dataToSave =[];
                for (i = 0; i < data.length; i++) {
                    dataToSave[i] = data[i];
                    $('#filteredItems').append('<li class="media" style="margin-top: 10px; text-align: center; width: 600px">\n' +
                        '                    <img class="mr-3" src="' + data[i].url + '" width="250px" height="220px" alt="Generic placeholder image" style=" border-radius: 9px; box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19);">\n' +
                        '                    <div class="media-body" style="box-shadow: 0 4px 8px 0 rgba(0, 0, 0, 0.2), 0 6px 20px 0 rgba(0, 0, 0, 0.19); background-color: #FFF2CD; height: 220px;">\n' +
                        '                        <a href="concreteItem.jsp" onclick="itemClicked(this.innerHTML)"><h2 class="mt-0 mb-1">' + data[i].name +  '</h2></a>\n' +
                        '                        <hr>\n' +
                        '                        <p style="font-size: x-large">Стоимость: ' + data[i].price +  '</p>\n' +
                        '                    </div>\n' +
                        '                </li>');
                }
            })
            .fail(function () {
                console.log("Failed");
            })
    });
});

function test(e) {
    var space = e.value.indexOf(' ');
    var filterName = e.value.substring(0, space);
    var filterValue = e.value.substring(space + 1);
    if (filterParams[filterName] == undefined) {
        filterParams[filterName] = '';
    }
    var previousLength = filterParams[filterName].length;
    if (filterParams[filterName].indexOf(filterValue) == -1) {
        if (filterParams[filterName] == '') {
            filterParams[filterName] = filterParams[filterName] + 'X' + filterValue + 'X';
        } else {
            filterParams[filterName] = filterParams[filterName] + ',X' + filterValue + 'X';
        }
    } else {
        var tmp = filterParams[filterName].substring(0, filterParams[filterName].indexOf(filterValue) - 2);
        var tmp2 = filterParams[filterName].substring(filterParams[filterName].indexOf(filterValue) + filterValue.length + 1, filterParams[filterName].length);
        filterParams[filterName] = tmp + tmp2;
        var s = filterParams[filterName];
        if (s[0] == ',') {
            s = s.substring(1, s.length);
            s = "" + s;
        }
        filterParams[filterName] = s;
    }

    var str = filterParams[filterName];
    str = str.replace(/X/g, '"');

    filtersMain = filtersMain.slice(0, filtersMain.indexOf(filterName) + 18 + filterName.length) + filtersMain.slice(filtersMain.indexOf(filterName) + 18 + filterName.length + previousLength);

    filtersMain = filtersMain.slice(0, filtersMain.indexOf(filterName) + 18 + filterName.length) + str + filtersMain.slice(filtersMain.indexOf(filterName) + 18 + filterName.length);
}

function itemClicked(e) {
    var res = e.substring(22);
    res = res.substring(0, res.length - 5);
    for (i = 0; i < dataToSave.length; i++) {
        if (dataToSave[i].name == res) {
            window.localStorage.setItem('selectedItemId', dataToSave[i].id);
        }
    }
}