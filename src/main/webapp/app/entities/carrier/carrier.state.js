(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('carrier', {
            parent: 'entity',
            url: '/carrier',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.carrier.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/carrier/carriers.html',
                    controller: 'CarrierController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carrier');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('carrier-detail', {
            parent: 'entity',
            url: '/carrier/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.carrier.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/carrier/carrier-detail.html',
                    controller: 'CarrierDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carrier');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Carrier', function($stateParams, Carrier) {
                    return Carrier.get({id : $stateParams.id});
                }]
            }
        })
        .state('carrier.new', {
            parent: 'carrier',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carrier/carrier-dialog.html',
                    controller: 'CarrierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('carrier', null, { reload: true });
                }, function() {
                    $state.go('carrier');
                });
            }]
        })
        .state('carrier.edit', {
            parent: 'carrier',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carrier/carrier-dialog.html',
                    controller: 'CarrierDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Carrier', function(Carrier) {
                            return Carrier.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('carrier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('carrier.delete', {
            parent: 'carrier',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/carrier/carrier-delete-dialog.html',
                    controller: 'CarrierDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Carrier', function(Carrier) {
                            return Carrier.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('carrier', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
