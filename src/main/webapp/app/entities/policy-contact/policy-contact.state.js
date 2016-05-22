(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('policy-contact', {
            parent: 'entity',
            url: '/policy-contact',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policyContact.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy-contact/policy-contacts.html',
                    controller: 'PolicyContactController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policyContact');
                    $translatePartialLoader.addPart('communicationPreference');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('policy-contact-detail', {
            parent: 'entity',
            url: '/policy-contact/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policyContact.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy-contact/policy-contact-detail.html',
                    controller: 'PolicyContactDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policyContact');
                    $translatePartialLoader.addPart('communicationPreference');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PolicyContact', function($stateParams, PolicyContact) {
                    return PolicyContact.get({id : $stateParams.id});
                }]
            }
        })
        .state('policy-contact.new', {
            parent: 'policy-contact',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-contact/policy-contact-dialog.html',
                    controller: 'PolicyContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                contactReferenceId: null,
                                deleted: null,
                                firstName: null,
                                lastName: null,
                                middleInitial: null,
                                dob: null,
                                workPhone: null,
                                homePhone: null,
                                emailAddress: null,
                                communicationPreference: null,
                                companyName: null,
                                externalReferenceId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('policy-contact', null, { reload: true });
                }, function() {
                    $state.go('policy-contact');
                });
            }]
        })
        .state('policy-contact.edit', {
            parent: 'policy-contact',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-contact/policy-contact-dialog.html',
                    controller: 'PolicyContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PolicyContact', function(PolicyContact) {
                            return PolicyContact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('policy-contact.delete', {
            parent: 'policy-contact',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-contact/policy-contact-delete-dialog.html',
                    controller: 'PolicyContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PolicyContact', function(PolicyContact) {
                            return PolicyContact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy-contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
