$(document).ready(function () {

    $("#validateSubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        validateSubmit();

    });

    $("#displaySubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        displaySubmit();

    });

});

function validateSubmit() {

    var data = $('#jsonDataForValidation').val();

    $("#validateSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/json/validate",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            $("#validateResult").text(data);
            console.log("SUCCESS : ", data);
            $("#validateSubmit").prop("disabled", false);

        },
        error: function (e) {

            $("#validateResult").text(e.responseText);
            console.log("ERROR : ", e);
            $("#validateSubmit").prop("disabled", false);

        }
    });
}

function displaySubmit() {

    var data = $('#jsonDataForDisplay').val();

    $("#displaySubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/json/display",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            $("#displayResult").html(data);
            console.log("SUCCESS : ", data);
            $("#displaySubmit").prop("disabled", false);

        },
        error: function (e) {

            $("#displayResult").text(e.responseText);
            console.log("ERROR : ", e);
            $("#displaySubmit").prop("disabled", false);

        }
    });
}