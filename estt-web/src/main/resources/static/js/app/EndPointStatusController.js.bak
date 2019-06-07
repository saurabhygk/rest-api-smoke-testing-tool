'use strict'

var module = angular.module('dashboard.controllers', []);
module.controller("EndPointStatusController", function($scope,  $interval, $window, EndPointStatusService) {
	$scope.Timer = null;
	
	function cancelInterval() {
		if (angular.isDefined($scope.Timer)) {
            $interval.cancel($scope.Timer);
        }
	}
	
	$scope.callAtInterval = function() {
		var userId = localStorage.getItem("userId");
		document.getElementById("userId").innerHTML = userId;
		
		EndPointStatusService.getEpStatusForPieChart(userId).then(function(value) {
					if (value.data.length == 0) {
						cancelInterval();
					}
					const json = JSON.stringify(value.data) || {};
					const { EC, SC, status } = JSON.parse(json);
					$scope.status = status;
					
					if(EC === null && SC === null && status == null) {
						$scope.labels = ["NO DATA FOUND FOR USER ID"];
						$scope.data = [userId];
						// intentionally not cancelled interval. As some user initiated process and would like to view progress and may be it will take some time to be displayed.
					} else {
						if (EC === null && SC !== null) {
							$scope.labels = ["SUCCESS"];
							$scope.data = [SC];
							$scope.colours = ["#72C02C"];
						} else if (SC === null && EC !== null) {
							$scope.labels = ["ERROR"];
							$scope.data = [EC];
							$scope.colours = ["#ff0000"];
						} else if(SC !== null && EC !== null) {
							$scope.labels = ["SUCCESS", "ERROR"];
							$scope.data = [SC, EC];
							$scope.colours = ['#72C02C', '#ff0000'];
						}
						
						if (status === 1){
							cancelInterval();
						}
					}
				}, function(reason) {
					console.log("error occured --> "+reason);
					$scope.status = 2;
					cancelInterval();
				}, function(value) {
					console.log("no callback");
					cancelInterval();
				});
		
	};
		
	$scope.Timer = $interval( function(){ $scope.callAtInterval(); }, 3000);

	$scope.$on('chart-create', function(event, instance){
	    // used to obtain chart instance
		$scope.chart = instance.chart;
	});
	
	 $scope.onclick = function(points, evt) {
		 
		 if (points.length == 1) {
			localStorage.setItem("status", points[0]._view.label);
		 	var url = "http://" + $window.location.host + "/listStatusDetails";
	         $window.location.href = url;
		 	// TODO: without pagination table, need to solve issue of pagination
	     }/* else {
	    	 localStorage.getItem("status", "");
	    	 var url = "http://" + $window.location.host + "/listStatusDetails";
	         $window.location.href = url;
	     }*/
	 }
});