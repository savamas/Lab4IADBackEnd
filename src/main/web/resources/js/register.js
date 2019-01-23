$('document').ready(function () {
    document.getElementById("registerFormSubmit").onsubmit = function() {

        var params = {
            username: $("#username").val(),
            password: $("#password").val(),
            firstName: $("#firstName").val(),
            lastName: $("#lastName").val(),
            sex: $("#sex").val(),
            phoneNum: $("#number").val()
        };

        $.ajax({
            type: "POST",
            url: "http://localhost:8080/Lab4IADBackEnd_Web_exploded/resource/users/register",
            contentType: "application/json",
            data: JSON.stringify(params),
            success: function (data) {
                if (data.registerStatus !== undefined) {
                    $("#registerAlert").append("<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "  Registration successful!\n" +
                        "</div>");
                    window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/login.jsp";
                } else {
                    $("#registerAlert").append("<div class=\"alert alert-danger\" role=\"alert\">\n" +
                         data.error +
                        "</div>");
                }
            }
        });
        return false;
    };
});