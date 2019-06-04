'use strict'

var module = angular.module('dashboard.controllers', []);
module.controller("HomeController", function($scope, $window) {
	$scope.displayProgressOfInitaitedUser = function () {
        if (typeof ($scope.userId) == "undefined" || $scope.userId == "") {
            $window.alert("Please enter user id ..!!!");
            return;
        }
        localStorage.setItem("userId", $scope.userId);
        var url = "http://" + $window.location.host + "/progress";
        $window.location.href = url;
    }
	
	$scope.createEsstConfigFile = function () {
        var url = "http://" + $window.location.host + "/createEsttConfigFile";
        $window.location.href = url;
    }
	
	$scope.formData = {}
	  $scope.serialize = function($event){
	    console.log($scope.formData)
	    //this.formDataStore.push($scope.formData);
	    alert(JSON.stringify($scope.formData))
	    $event.preventDefault()
	  }
	
});