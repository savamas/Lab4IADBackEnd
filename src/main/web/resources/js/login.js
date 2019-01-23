$('document').ready(function () {
    document.getElementById("loginFormSubmit").onsubmit = function() {

      //  $("#loginAlert").remove();

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