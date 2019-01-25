$('document').ready(function () {
    $( "#leaveAccount" ).click(function() {
        window.localStorage.removeItem('token');
        window.location = "http://localhost:8080/Lab4IADBackEnd_Web_exploded/index.jsp";
    });
});