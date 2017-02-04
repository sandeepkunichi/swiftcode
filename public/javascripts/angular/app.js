var app = angular.module('swiftCodeApp', []);
app.controller('testController', function($scope, $http, $filter) {
    $scope.test = {};
    $scope.test.testQuestions = [];
    $scope.test.testPrograms = [];
    $scope.LanguageTypeList = [];

    var request = $http({
        method: "GET",
        url: "/languages"
    });
    request.success(function(data) {
        $scope.LanguageTypeList = data;
    });

    $scope.addNewQuestion = function() {
        var testQuestion = {
            testAnswers : []
        };
        $scope.test.testQuestions.push(testQuestion);
    };

    $scope.removeQuestion = function (index) {
        $scope.test.testQuestions.splice(index, 1);
    };

    $scope.addNewAnswer = function(testQuestion) {
        var testAnswer = {};
        testQuestion.testAnswers.push(testAnswer);
    };

    $scope.removeAnswer = function (testQuestion, index) {
        testQuestion.testAnswers.splice(index, 1);
    };

    $scope.createTest = function(){
        var request = $http({
            method: "POST",
            url: "/test/create",
            data: $scope.test
        });
        request.success(function(data) {
            location.reload();
        });
        request.error(function(data) {
            alert("Something went wrong while creating the test");
        });
    }

    $scope.updateTest = function(){
        var request = $http({
            method: "POST",
            url: "/test/edit",
            data: $scope.test
        });
        request.success(function(data) {
            location.reload();
        });
        request.error(function(data) {
            alert("Something went wrong while updating the test");
        });
    }

    $scope.addNewProgrammingQuestion = function(){
        var testProgram = {
           programQuestion : ""
        };
        $scope.test.testPrograms.push(testProgram);
    }

    $scope.removeProgrammingQuestion = function (index) {
        $scope.test.testPrograms.splice(index, 1);
    };

    $scope.removeLanguage = function(language) {
        var index = $scope.test.codeSessionConfiguration.languages.indexOf(language);
        $scope.test.codeSessionConfiguration.languages.splice(index, 1);
    };

    $scope.addLanguage = function(language) {
        if(!$scope.test.codeSessionConfiguration.languages){
            $scope.test.codeSessionConfiguration.languages = [];
        }
        $scope.test.codeSessionConfiguration.languages.push(language);
    };

    $scope.alreadySupported = function(language){
        var filter = true;
        angular.forEach($scope.test.codeSessionConfiguration.languages, function(item){
            if(language.id == item.id){
                filter = false;
            }
        });
        return filter;
    }

});