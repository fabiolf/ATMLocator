angular.module('ATMLocatorApp').config(['$locationProvider', '$httpProvider', function($locationProvider, $httpProvider) {
	/* Register error provider that shows message on failed requests or shows login alert on
	 * unauthenticated requests */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
	        return {
	        	'responseError': function(rejection) {
	        		var status = rejection.status;
	        		var config = rejection.config;
	        		var method = config.method;
	        		var url = config.url;
	      
	        		if (status == 401) {
	        			//$location.path( "/atmlocation-0.1.0/login" );
	        			console.log("Got an error status 401. Proceeding to login.")
	        			$rootScope.login();
	        		}
	              
	        		return $q.reject(rejection);
	        	}
	        };
	    }
    );
    
    /* Registers auth token interceptor, auth token is either passed by header or by query parameter
     * as soon as there is an authenticated user */
    $httpProvider.interceptors.push(function ($q, $rootScope, $location) {
        return {
        	'request': function(config) {
        		if (angular.isDefined($rootScope.authToken)) {
        			var authToken = $rootScope.authToken;
//        			if (exampleAppConfig.useAuthTokenHeader) {
        				config.headers['X-Auth-Token'] = authToken;
//        			} else {
//        				config.url = config.url + "?token=" + authToken;
//        			}
        		}
        		return config || $q.when(config);
        	}
        };
    }
);
}]).run(function($rootScope, $location, /*$cookieStore,*/ UserService) {
	
	/* Reset error when a new view is loaded */
//	$rootScope.$on('$viewContentLoaded', function() {
//		delete $rootScope.error;
//	});
	
	$rootScope.hasRole = function(role) {
		
		if ($rootScope.user === undefined) {
			return false;
		}
		
		if ($rootScope.user.roles[role] === undefined) {
			return false;
		}
		
		return $rootScope.user.roles[role];
	};
	
	$rootScope.logout = function() {
		delete $rootScope.user;
		delete $rootScope.authToken;
//		$cookieStore.remove('authToken');
		$rootScope.login();
	};
	
	$rootScope.login = function() {
		//show alert to get user and password
		var user = prompt('Please provide username');
		var pass = prompt('Please provide password for user ' + user);
		$rootScope.user = user;
		$rootScope.authToken = pass;
		/*
		UserService.authenticate($.param({username: user, password: pass})).$promise.then(function(authenticationResult) {
			console.log(authenticationResult);
			$rootScope.authToken = authenticationResult; //for now just a string with the token that is the password....
			$rootScope.user = user;
		}).catch(function(data) {
	        // Handle error here
			msg = 'ERROR! Could not authenticate user ' + user + ' (the call to http://localhost:' + serverPort + '/api/user/authenticate failed). Please verify.';
			alert(msg);
		});
		*/
		
	};
	
//	var originalPath = $location.path();
//	$location.path("/login");
	$rootScope.login();
/*
	if ($rootScope.authToken !== undefined) {
		//$rootScope.authToken = authToken;
		UserService.get().$promise.then(function(user) {
			$rootScope.user = user;
//			$location.path(originalPath);
		}).catch(function(data) {
			// Handle error here
			msg = 'ERROR! Could not access backend to authenticate user (the call to http://localhost:' + serverPort + 'atmlocator-0.1.0/api/user/' + cityName + ' failed). Please verify.';
			alert(msg);
		});
	}
*/	
});;