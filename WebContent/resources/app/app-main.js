var myPollApp = angular.module('myPollApp', ['ngRoute', 'ngAnimate', 'ui.bootstrap', 'chart.js']);

myPollApp.run(function($rootScope, $location) {
    //remove at production
    $rootScope.loggedIn = true;
    $rootScope.$on("$routeChangeStart", function(vent, next, current) {
        if (!$rootScope.loggedIn) {
            if (next.templateUrl !== "resources/views/login.html" &&
                next.templateUrl !== 'resources/views/home.html') {
                $location.path("/login");
            }
        }
    });
});

myPollApp.config(function($routeProvider, $locationProvider) {

    $locationProvider.html5Mode(true);

    $routeProvider
        .when('/home', {
            templateUrl: 'resources/views/home.html',
            controller: 'HomeController'
        })
        .when('/login', {
            templateUrl: 'resources/views/login.html',
            controller: 'LoginController'
        })
        .when('/contactlist', {
            templateUrl: 'resources/views/contactlist-listview.html',
            controller: 'ContactListViewController'
        })
        .when('/contactlist/:contactListId', {
            templateUrl: 'resources/views/contactlist-edit.html',
            controller: 'ContactListEditController',
            resolve: {
                contactList: function(ContactListService, $route) {
                    return ContactListService.getContactList($route.current.params.contactListId).then(function(response) {
                        return response.data;
                    },function(response){
                        return null;
                    });
                }
            }
        })
        .when('/new-poll', {
            templateUrl: 'resources/views/new-poll.html',
            controller: 'NewPollController',
            resolve: {
                contactLists: function(ContactListService) {
                    return ContactListService.getAllContactlists().then(function successCallback(response) {
                        return response.data;
                    });
                }
            }
        })
        .when('/poll', {
            templateUrl: 'resources/views/poll-listview.html',
            controller: 'PollListViewController',
            resolve: {
                polls: function(PollService) {
                    return PollService.getAllPolls().then(function successCallback(response) {
                        return response.data;
                    });
                }
            }
        })
        .when('/poll/:pollId', {
            templateUrl: 'resources/views/poll-individual-view.html',
            controller: 'PollIndividualViewController',
            resolve: {
                poll: function(PollService, $route) {
                    return PollService.getPoll($route.current.params.pollId).then(function successCallback(response) {
                        return response.data;
                    });
                }
            }
        })
        .otherwise({
            redirectTo: '/home'
        })
});

myPollApp.filter("timestampFilter",function(){
    return function(timestamp){
        if (timestamp) {
            var t = timestamp.split(/[-T:+]/);
            var d = new Date(t[0], t[1]-1, t[2], t[3], t[4], t[5]);
            var dateString = d.getDate() + "/" + d.getMonth() + " " + d.getHours() + ":" + d.getMinutes();
            return dateString;
        } else {
            return "";
        }
    }
});
