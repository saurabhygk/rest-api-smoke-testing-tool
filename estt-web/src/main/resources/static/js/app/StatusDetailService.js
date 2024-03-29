'use strict'

angular.module('dashboard.services', []).factory('StatusDetailService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			service.listStatusDetails = function(status, userId) {
				var url = CONSTANTS.listStatusDetails + status +"&userId="+userId;
				return $http.get(url);
			}
			return service;
		} ]);