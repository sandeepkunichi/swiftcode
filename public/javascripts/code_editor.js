/// <reference path="jquery-3.1.1.min.js" />
/// <reference path="p5.js" />
/// <reference path="three.js" />
/// <reference path="perlin.js" />
/// <reference path="stats.min.js" />
/// <reference path="controls/OrbitControls.js" />

var ACCESS_TOKEN = "e48b867d60348856ec018a8fb62126fa";
var interval = null;

function getOutput(submissionID) {
    $.get(
        'http://f939607f.compilers.sphere-engine.com/api/v3/submissions/' + submissionID + '?access_token=' + ACCESS_TOKEN,
        {
            withSource: true,
            withInput: true,
            withOutput: true,
            withStderr: true,
        },
        function (data, status) {
            var jsonOBJ = $.parseJSON(data);
            console.log("JSON: " + data + "\nStatus: " + status);

            if (jsonOBJ.status == 0 && jsonOBJ.result == 15) {
                $("#code-output").text(jsonOBJ.output);
                clearInterval(interval);
            }
        }
    );
}

$(document).ready(function (e) {

    $('#code-form-submit').click(function () {
        var sourceCode = $("#code-text").val();
        var language = $("#code-lang option:selected").val();
        console.log(sourceCode);
        console.log(language);

        $.post(
            'http://f939607f.compilers.sphere-engine.com/api/v3/submissions?access_token=' + ACCESS_TOKEN,
            {
                sourceCode: sourceCode,
                language: language,
                input: ""
            },
            function (data, status) {

                var jsonOBJ = $.parseJSON(data);
                var submissionID = jsonOBJ.id;
                console.log("ID: " + submissionID + "\nStatus: " + status);

                interval = setInterval(function () {
                    getOutput(submissionID);
                }, 1000);
            }
        );
    });




});
