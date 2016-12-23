myPollApp.factory('PollService', function($http) {
    var obj = {};
    var baseUrl = 'api/poll';

    obj.sendPoll = function(newPoll) {
        return $http.post(baseUrl, newPoll);
    }

    obj.getAllPolls = function(){
        return $http.get(baseUrl);
    }

    obj.getPoll = function(pollId){
        var url = baseUrl + "/" + pollId;
        return $http.get(url);
    }

    obj.updateResponses = function(pollId){
        var url = baseUrl + "/" + pollId + "?updateResponses=true";
        return $http.get(url);
    }

    obj.deletePoll = function(pollId){
        var url = baseUrl + "/" + pollId;
        return $http.delete(url);
    }

    return obj;
});
