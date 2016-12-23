myPollApp.factory('ContactListService', function($http) {
    var obj = {};
    var baseUrl = 'api/contactlist';

    obj.getAllContactlists = function() {
        return $http.get(baseUrl);
    }

    obj.getContactList = function(contactListId) {
        var url = baseUrl + "/" + contactListId;
        return $http.get(url);
    }

    obj.editContactList = function(contactList) {
        var url = baseUrl + "/" + contactList.contactListId;
        return $http.put(url,contactList);
    }

    obj.createContactList = function(newContactList) {
        return $http.post(baseUrl, newContactList);
    }

    obj.deleteContactList = function(contactList) {
        var url = baseUrl + "/" + contactList.contactListId;
        return $http.delete(url);
    }

    return obj;
});
