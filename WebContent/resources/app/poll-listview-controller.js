myPollApp.controller('PollListViewController', function($scope, $uibModal, PollService, polls) {
    $scope.polls = polls;
});

myPollApp.filter("repliedMessagesFilter",function(){
    return function(messages){
        var len = messages.length;
        var repliedMessages = [];
        for (var i=0; i<len;i++){
            if (messages[i].smsResponses.length > 0){
                repliedMessages.push(messages[i]);
            }
        }
        return repliedMessages;
    }
});
