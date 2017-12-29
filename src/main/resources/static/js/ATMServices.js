var services = angular.module('ATMLocator.services', ['ngResource']);

var serverPort = 8085;


services.factory('UserService', function($resource) {
	
	return $resource('http://localhost:' + serverPort + '/atmlocator-0.1.0/api/user/:action', {},
			{
				authenticate: {
					method: 'POST',
					params: {'action' : 'authenticate'},
					headers : {'Content-Type': 'application/x-www-form-urlencoded'}
				},
			}
		);
});

services.factory('LocationService', function($resource) {
	return $resource('http://localhost:' + serverPort + '/atmlocator-0.1.0/api/list/:name', {name: '@cityName'});
});