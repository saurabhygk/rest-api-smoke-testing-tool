'use strict'

var module = angular.module('dashboard.controllers', []);
module.controller("EndPointStatusController", function($scope,  $interval, $window, EndPointStatusService) {
	$scope.Timer = null;
	var statusGlobal;
	function cancelInterval() {
		if (angular.isDefined($scope.Timer)) {
            $interval.cancel($scope.Timer);
        }
	}
	$scope.abortSynch = function() {
		cancelInterval();
		var url = "http://" + $window.location.host;
        $window.location.href = url;
	}
	var userId ;
	$scope.callAtInterval = function() {
		userId = localStorage.getItem("userId");
		document.getElementById("userId").innerHTML = userId;
		
		EndPointStatusService.getEpStatusForPieChart(userId).then(function(value) {
					if (value.data.length == 0) {
						cancelInterval();
					}
					$scope.noDatafound = 0;
					const json = JSON.stringify(value.data) || {};
					const { EC, SC, status, statusFile} = JSON.parse(json);
					$scope.status = status;
					statusGlobal =  status;
					console.log(EC);
					console.log(SC);
					console.log(status);
					console.log(statusFile);
					if(EC === null && SC === null && status == null) {
						$scope.noDatafound = 1;
						// intentionally not cancelled interval. As some user initiated process and would like to view progress and may be it will take some time to be displayed.
					} else {
						$scope.options={
						      legend: {
						            display: true,
						            position: "bottom"
						        },
						        tooltipEvents: [],
						        showTooltips: true,
						        tooltipCaretSize: 0,
						        onAnimationComplete: function() {
						            this.showTooltip(this.segments, true);
						        },
						    };
						if (EC === null && SC !== null) {
							$scope.labels = ["SUCCESS"];
							$scope.data = [SC];
							$scope.colours = ["#72C02C"];
							$scope.series= [SC];
						} else if (SC === null && EC !== null) {
							$scope.labels = ["ERROR"];
							$scope.data = [EC];
							$scope.colours = ["#ff0000"];
							$scope.series= [EC];
						} else if(SC !== null && EC !== null) {
							$scope.labels = ["SUCCESS", "ERROR"];
							$scope.data = [SC, EC];
							$scope.colours = ['#72C02C', '#ff0000'];
							$scope.series= [SC, EC];
						}
						if (status === 1){
							$scope.status = 3;
							$scope.fileCreated = 1;
							$scope.statusFile = statusFile;
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
		 
		 if (statusGlobal == 1 && points.length == 1) {
			localStorage.setItem("status", points[0]._view.label);
			localStorage.setItem("userId", userId);
		 	var url = "http://" + $window.location.host + "/listStatusDetails";
	         $window.location.href = url;
		 	// TODO: without pagination table, need to solve issue of pagination
	     }
	 }
});