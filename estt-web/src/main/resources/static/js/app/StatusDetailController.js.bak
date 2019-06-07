'use strict'

var module = angular.module('dashboard.controllers', []);
module.controller("StatusDetailController", function($scope, $filter,
		StatusDetailService) {
	var status = localStorage.getItem("status");
	$scope.curPage = 1, $scope.itemsPerPage = 3, $scope.maxSize = 5;
	var items = [];
	StatusDetailService.listStatusDetails(status).then(function(value) {
		if (value.data.length == 0) {
		}
		
		items = value.data;

		$scope.$watch('curPage + numPerPage', function() {
			var begin = (($scope.curPage - 1) * $scope.itemsPerPage), end = begin
					+ $scope.itemsPerPage;

			$scope.filteredItems = value.data.slice(begin, end);

		});
	}, function(reason) {
		console.log("error occured --> " + reason);
		$scope.status = 2;
	}, function(value) {
		console.log("no callback");
	});
	
	//this.items = value.data;
	$scope.numOfPages = function() {
		return Math.ceil(value.data.length / $scope.itemsPerPage);
	};
	
});