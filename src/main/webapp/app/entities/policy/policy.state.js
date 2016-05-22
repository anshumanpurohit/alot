(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('policy', {
            parent: 'entity',
            url: '/policy',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy/policies.html',
                    controller: 'PolicyController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('policy-detail', {
            parent: 'entity',
            url: '/policy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.policy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/policy/policy-detail.html',
                    controller: 'PolicyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('policy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Policy', function($stateParams, Policy) {
                    return Policy.get({id : $stateParams.id});
                }]
            }
        })
        .state('policy.new', {
            parent: 'policy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy/policy-dialog.html',
                    controller: 'PolicyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                policyNumber: null,
                                baseState: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('policy', null, { reload: true });
                }, function() {
                    $state.go('policy');
                });
            }]
        })
        .state('policy.edit', {
            parent: 'policy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy/policy-dialog.html',
                    controller: 'PolicyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Policy', function(Policy) {
                            return Policy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('policy.delete', {
            parent: 'policy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/policy/policy-delete-dialog.html',
                    controller: 'PolicyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Policy', function(Policy) {
                            return Policy.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('policy', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
