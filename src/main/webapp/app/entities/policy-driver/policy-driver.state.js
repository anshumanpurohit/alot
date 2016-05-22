(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('policy-driver', {
            parent: 'entity',
            url: '/policy-driver',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policyDriver.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy-driver/policy-drivers.html',
                    controller: 'PolicyDriverController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policyDriver');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('policy-driver-detail', {
            parent: 'entity',
            url: '/policy-driver/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policyDriver.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy-driver/policy-driver-detail.html',
                    controller: 'PolicyDriverDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policyDriver');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PolicyDriver', function($stateParams, PolicyDriver) {
                    return PolicyDriver.get({id : $stateParams.id});
                }]
            }
        })
        .state('policy-driver.new', {
            parent: 'policy-driver',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-driver/policy-driver-dialog.html',
                    controller: 'PolicyDriverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('policy-driver', null, { reload: true });
                }, function() {
                    $state.go('policy-driver');
                });
            }]
        })
        .state('policy-driver.edit', {
            parent: 'policy-driver',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-driver/policy-driver-dialog.html',
                    controller: 'PolicyDriverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PolicyDriver', function(PolicyDriver) {
                            return PolicyDriver.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy-driver', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('policy-driver.delete', {
            parent: 'policy-driver',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy-driver/policy-driver-delete-dialog.html',
                    controller: 'PolicyDriverDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PolicyDriver', function(PolicyDriver) {
                            return PolicyDriver.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy-driver', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
