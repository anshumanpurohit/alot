(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('address-book', {
            parent: 'entity',
            url: '/address-book',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.addressBook.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address-book/address-books.html',
                    controller: 'AddressBookController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('addressBook');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('address-book-detail', {
            parent: 'entity',
            url: '/address-book/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.addressBook.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/address-book/address-book-detail.html',
                    controller: 'AddressBookDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('addressBook');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AddressBook', function($stateParams, AddressBook) {
                    return AddressBook.get({id : $stateParams.id});
                }]
            }
        })
        .state('address-book.new', {
            parent: 'address-book',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-dialog.html',
                    controller: 'AddressBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                deleted: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: true });
                }, function() {
                    $state.go('address-book');
                });
            }]
        })
        .state('address-book.edit', {
            parent: 'address-book',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-dialog.html',
                    controller: 'AddressBookDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AddressBook', function(AddressBook) {
                            return AddressBook.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('address-book.delete', {
            parent: 'address-book',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/address-book/address-book-delete-dialog.html',
                    controller: 'AddressBookDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AddressBook', function(AddressBook) {
                            return AddressBook.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('address-book', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
