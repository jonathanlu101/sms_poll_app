myPollApp.controller('ContactListEditController', function($scope, $uibModal, ContactListService, contactList) {

    if (contactList){
        $scope.contactList = contactList;
    }else{
        $scope.errorMessage = {
            message: "Error: Couldn't retrieve Contact List.",
            redirectLink: "/contactlist",
            redirectText: "Return to ContactList"
        };
    }

    $scope.openDeleteContactListModal = function(contactList) {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/deleteContactListModal.html',
            controller: 'deleteContactListModalInstanceCtrl',
            controllerAs: '$ctrl',
            resolve: {
                contactList: function() {
                    return contactList;
                }
            }
        });
        modalInstance.result.then(function(contactList) {
            ContactListService.deleteContactList(contactList).then(function successCallback(response) {
                $scope.successMessage = {
                    message:  "Contact List " + contactList.name + " has been deleted.",
                    redirectLink: "/contactlist",
                    redirectText: "Return to ContactList"
                };
                $scope.contactList = null;
            }, function errorCallback(response) {
                $scope.errorMessage = {
                    message: "Deletion failed, please try again later."
                };
            });
        });
    };

    $scope.openEditContactListModal = function(originalContactList) {
        editedContactList = $.extend(true, {}, originalContactList);
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/editContactListModal.html',
            controller: 'editContactListModalInstanceCtrl',
            controllerAs: '$ctrl',
            resolve: {
                editedContactList: function() {
                    return editedContactList;
                }
            }
        });
        modalInstance.result.then(function(editedContactList) {
            ContactListService.editContactList(editedContactList).then(function successCallback(response) {
                $scope.successMessage = {
                    message:  "Contact List " + contactList.name + " has been updated."
                };
                $scope.contactList = response.data;
            }, function errorCallback(response) {
                $scope.errorMessage = {
                    message: "Update failed, please try again later."
                };
            });
        });
    };

    $scope.openCreateContactModal = function() {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/createContactModal.html',
            controller: 'createContactModalInstanceCtrl',
            controllerAs: '$createContact',
            resolve: {
                contactList : function(){return $scope.contactList;}
            }
        });

        modalInstance.result.then(function(newContact) {
            $scope.contactList.contacts.push(newContact);
            $scope.successMessage = {
                message: newContact.firstName + " " + newContact.lastName + " has been added to the list."
            };
        });
    };

    $scope.openEditContactModal = function(contact) {
        var modalInstance = $uibModal.open({
            animation: true,
            ariaLabelledBy: 'modal-title',
            ariaDescribedBy: 'modal-body',
            templateUrl: 'resources/templates/editContactModal.html',
            controller: 'editContactModalInstanceCtrl',
            controllerAs: '$editContact',
            resolve: {
                originalContact : function(){return contact;},
                contactList: function(){return contactList;}
            }
        });

        modalInstance.result.then(function(editedContact) {
            var index = contactList.contacts.indexOf(contact);
            contactList.contacts.splice(index,1,editedContact);
            $scope.successMessage = {
                message: "Contact has been edited."
            };
        });
    };

    $scope.deleteContact = function(contact){
        var index = $scope.contactList.contacts.indexOf(contact);
        $scope.contactList.contacts.splice(index,1);
    }

    $scope.saveAllChanges = function() {
        ContactListService.editContactList($scope.contactList).then(function successCallback(response) {
            $scope.successMessage = {
                message:  "Contact List " + contactList.name + " has been updated."
            };
            $scope.contactList = response.data;
        }, function errorCallback(response) {
            $scope.errorMessage = {
                message: "Update failed, contact changes are not saved. Save your work and refresh."
            };
        });
    }
})

myPollApp.controller('createContactModalInstanceCtrl', function($uibModalInstance, contactList) {

    $createContact = this;
    $createContact.contactList = contactList;

    $createContact.create = function(newContact) {
        var error = false;
        var mobileRegex = new RegExp(/^0{1}4{1}\d{8}$/);

        if (!mobileRegex.test(newContact.mobile)){
            error = "Mobile must be in the format of 04XXXXXXXX";
        }
        for (var i=0; i < contactList.contacts.length; i++){
            var contact = contactList.contacts[i];
            if (contact.firstName === newContact.firstName && contact.lastName === newContact.lastName){
                error = "Contact with the same first name and last name already exists";
            }
            if (contact.mobile === newContact.mobile){
                error = "Contact with the same mobile already exists";
            }
        }

        if (error){
            $createContact.errorMessage = "Error: " + error;
        }else{
            $uibModalInstance.close(newContact);
        }
    };

    $createContact.cancel = function() {
        $uibModalInstance.dismiss();
    };
});

myPollApp.controller('editContactModalInstanceCtrl', function($uibModalInstance, originalContact, contactList) {

    $editContact = this;
    $editContact.editedContact = $.extend(true, {}, originalContact);

    $editContact.update = function(editedContact) {
        var error = false;
        var mobileRegex = new RegExp(/^0{1}4{1}\d{8}$/);
        var indexOfEditedContact = contactList.contacts.indexOf(originalContact);

        if (!mobileRegex.test(editedContact.mobile)){
            error = "Mobile must be in the format of 04XXXXXXXX";
        }
        for (var i=0; i < contactList.contacts.length; i++){
            if (i !== indexOfEditedContact){
                var contact = contactList.contacts[i];
                if (contact.firstName === editedContact.firstName && contact.lastName === editedContact.lastName){
                    error = "Contact with the same first name and last name already exists";
                }
                if (contact.mobile === editedContact.mobile){
                    error = "Contact with the same mobile already exists";
                }
            }
        }
        if (error){
            $editContact.errorMessage = "Error: " + error;
        }else{
            $uibModalInstance.close(editedContact);
        }
    };

    $editContact.reset = function(){
        $editContact.editedContact = $.extend(true, {}, originalContact);
    }

    $editContact.cancel = function() {
        $uibModalInstance.dismiss();
    };
});
