'use strict'

angular.module('dashboard.services', []).factory('StatusDetailService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			service.listStatusDetails = function(status) {
				var url = CONSTANTS.listStatusDetails + status;
				return $http.get(url);
			}
			return service;
		} ]);