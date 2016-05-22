(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage-def', {
            parent: 'entity',
            url: '/coverage-def',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageDef.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-def/coverage-defs.html',
                    controller: 'CoverageDefController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageDef');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-def-detail', {
            parent: 'entity',
            url: '/coverage-def/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageDef.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-def/coverage-def-detail.html',
                    controller: 'CoverageDefDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageDef');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoverageDef', function($stateParams, CoverageDef) {
                    return CoverageDef.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage-def.new', {
            parent: 'coverage-def',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-def/coverage-def-dialog.html',
                    controller: 'CoverageDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                beginEffectiveDate: null,
                                endEffectiveDate: null,
                                state: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('coverage-def', null, { reload: true });
                }, function() {
                    $state.go('coverage-def');
                });
            }]
        })
        .state('coverage-def.edit', {
            parent: 'coverage-def',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-def/coverage-def-dialog.html',
                    controller: 'CoverageDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoverageDef', function(CoverageDef) {
                            return CoverageDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage-def.delete', {
            parent: 'coverage-def',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-def/coverage-def-delete-dialog.html',
                    controller: 'CoverageDefDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoverageDef', function(CoverageDef) {
                            return CoverageDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
