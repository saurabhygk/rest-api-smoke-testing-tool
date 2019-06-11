'use strict'

var dashboardApp = angular.module('dashboard', [ 'ui.bootstrap', 'dashboard.controllers',  'chart.js',
		'dashboard.services' ]);
dashboardApp.constant("CONSTANTS", {
	getEpStatusForPieChart : "/real-time-data/endpointsStatusCount?userId=",
	listStatusDetails : "/real-time-data/listStatusDetails?status=",
	deleteStatusFile : "/real-time-data/deleteStatusFile?userId="
});