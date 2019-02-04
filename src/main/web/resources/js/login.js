$('document').ready(function () {

    if (window.localStorage.getItem('token') !== null) {
        document.getElementById('onlyForLoggedUsers').innerHTML = "<a class=\"nav-link\" href=\"account.jsp\" style=\"color: #F3ECD6; font-family: Rockwell; font-size: 25px;\">Личный кабинет</a>";
    }

    document.getElementById("loginFormSubmit").onsubmit = function() {

        var params = {
            username: $("#userEmail").val(),
            password: $("#userPassword").val()
        };

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/users/authenticate",
            contentType: "application/json",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.token !== undefined) {
                    $("#loginAlert").append("<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "  Authorization successful!\n" +
                        "</div>");
                    window.localStorage.setItem('token', data.token);
                    //window.localStorage.setItem('occupation', data.occupation);
                    if (data.occupation == "Courier") {
                        window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/backOffice.jsp";
                    } else {
                        window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/index.jsp";
                    }
                } else {
                    $("#loginAlert").append("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                        "  Incorrect username or login!\n" +
                        "</div>");
                }
            }
        });
        return false;
    };
});