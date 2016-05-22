(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('named-insured', {
            parent: 'entity',
            url: '/named-insured',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.namedInsured.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/named-insured/named-insureds.html',
                    controller: 'NamedInsuredController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('namedInsured');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('named-insured-detail', {
            parent: 'entity',
            url: '/named-insured/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.namedInsured.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/named-insured/named-insured-detail.html',
                    controller: 'NamedInsuredDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('namedInsured');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'NamedInsured', function($stateParams, NamedInsured) {
                    return NamedInsured.get({id : $stateParams.id});
                }]
            }
        })
        .state('named-insured.new', {
            parent: 'named-insured',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/named-insured/named-insured-dialog.html',
                    controller: 'NamedInsuredDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                deleted: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('named-insured', null, { reload: true });
                }, function() {
                    $state.go('named-insured');
                });
            }]
        })
        .state('named-insured.edit', {
            parent: 'named-insured',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/named-insured/named-insured-dialog.html',
                    controller: 'NamedInsuredDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['NamedInsured', function(NamedInsured) {
                            return NamedInsured.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('named-insured', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('named-insured.delete', {
            parent: 'named-insured',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/named-insured/named-insured-delete-dialog.html',
                    controller: 'NamedInsuredDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['NamedInsured', function(NamedInsured) {
                            return NamedInsured.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('named-insured', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
