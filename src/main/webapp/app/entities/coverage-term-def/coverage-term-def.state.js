(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage-term-def', {
            parent: 'entity',
            url: '/coverage-term-def',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermDef.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-def/coverage-term-defs.html',
                    controller: 'CoverageTermDefController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermDef');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-term-def-detail', {
            parent: 'entity',
            url: '/coverage-term-def/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermDef.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-def/coverage-term-def-detail.html',
                    controller: 'CoverageTermDefDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermDef');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoverageTermDef', function($stateParams, CoverageTermDef) {
                    return CoverageTermDef.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage-term-def.new', {
            parent: 'coverage-term-def',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-def/coverage-term-def-dialog.html',
                    controller: 'CoverageTermDefDialogController',
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
                    $state.go('coverage-term-def', null, { reload: true });
                }, function() {
                    $state.go('coverage-term-def');
                });
            }]
        })
        .state('coverage-term-def.edit', {
            parent: 'coverage-term-def',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-def/coverage-term-def-dialog.html',
                    controller: 'CoverageTermDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoverageTermDef', function(CoverageTermDef) {
                            return CoverageTermDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage-term-def.delete', {
            parent: 'coverage-term-def',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-def/coverage-term-def-delete-dialog.html',
                    controller: 'CoverageTermDefDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoverageTermDef', function(CoverageTermDef) {
                            return CoverageTermDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
