(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contact', {
            parent: 'entity',
            url: '/contact',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.contact.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact/contacts.html',
                    controller: 'ContactController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contact');
                    $translatePartialLoader.addPart('communicationPreference');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contact-detail', {
            parent: 'entity',
            url: '/contact/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.contact.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contact/contact-detail.html',
                    controller: 'ContactDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contact');
                    $translatePartialLoader.addPart('communicationPreference');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contact', function($stateParams, Contact) {
                    return Contact.get({id : $stateParams.id});
                }]
            }
        })
        .state('contact.new', {
            parent: 'contact',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-dialog.html',
                    controller: 'ContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
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
                    $state.go('contact', null, { reload: true });
                }, function() {
                    $state.go('contact');
                });
            }]
        })
        .state('contact.edit', {
            parent: 'contact',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-dialog.html',
                    controller: 'ContactDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contact', function(Contact) {
                            return Contact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contact.delete', {
            parent: 'contact',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contact/contact-delete-dialog.html',
                    controller: 'ContactDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contact', function(Contact) {
                            return Contact.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('contact', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
