'use strict'

angular.module('dashboard.services', []).factory('HomeService',
		[ "$http", "CONSTANTS", function($http, CONSTANTS) {
			var service = {};
			service.displayProgressOfInitaitedUser = function() {
				var url = "/progress";
				return $http.get(url);
			}
			return service;
		} ]);