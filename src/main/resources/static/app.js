var appModule = angular.module('myApp', []);

appModule
	.controller(
		'MainCtrl',
		[
			'mainService',
			'$scope',
			'$http',
			function(mainService, $scope, $http) {
				
				$scope.greeting = 'Welcome to the JSON Web Token / AngularJR / Spring example!';
				$scope.token = localStorage.token == undefined ? null : localStorage.token;
				$scope.error = null;
				$scope.roleUser = localStorage.token == undefined ? null : jwt_decode(localStorage.token).roles.indexOf("user") >= 0 ? true : false;
				$scope.roleAdmin = localStorage.token == undefined ? null : jwt_decode(localStorage.token).roles.indexOf("admin") >= 0 ? true : false;
				$scope.roleFoo = localStorage.token == undefined ? null : jwt_decode(localStorage.token).roles.indexOf("foo") >= 0 ? true : false;
				$scope.userName = localStorage.token == undefined ? null : jwt_decode(localStorage.token).sub + "FromDecodedJwt";
				
				$scope.login = function() {

					$scope.error = null;
					mainService
							.login($scope.userLogin)
							.then(
								function(token) {
									$http.defaults.headers.common.Authorization = 'Bearer ' + token;
									$scope.checkRoles();
									
									// Add name when co for the first time
									$scope.userName = $scope.userLogin;

									$scope.token = token;
									// add token in local storage // add step 1
									localStorage.token = token;
									
								}, function(error) {
									$scope.error = error
									$scope.userName = '';
								});
				}

				$scope.checkRoles = function() {
					mainService.hasRole('user').then(
							function(user) {
								$scope.roleUser = user
							});
					mainService.hasRole('admin').then(
							function(admin) {
								$scope.roleAdmin = admin
							});
					mainService.hasRole('foo').then(function(foo) {
						$scope.roleFoo = foo
					});
				}

				$scope.logout = function() {
					$scope.userName = '';
					$scope.token = null;
					$http.defaults.headers.common.Authorization = '';
					
					// clean storage
					localStorage.removeItem("token"); // add step 1
				}

				$scope.loggedIn = function() {
					return $scope.token !== undefined && $scope.token !== null; // update step 1
				}
				
			} ])

	.service('mainService', function($http) {
		return {
			login : function(userLogin) {
				return $http.post('/user/login', {name : userLogin}).then(function(response) {
					console.log("response : " + response.data.token)
					return response.data.token;
				});
			},
	
			hasRole : function(role) {
				return $http.get('/api/role/' + role).then(function(response) {
					console.log(response);
					return response.data;
				});
			}
		};
	});
