'use strict'

angular.module('dashboard.services', []).factory('EndPointStatusService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			service.getEpStatusForPieChart = function(userId) {
				var url = CONSTANTS.getEpStatusForPieChart + userId;
				return $http.get(url);
			}
			return service;
		} ]);