(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage-term-option-def', {
            parent: 'entity',
            url: '/coverage-term-option-def',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermOptionDef.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-option-def/coverage-term-option-defs.html',
                    controller: 'CoverageTermOptionDefController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermOptionDef');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-term-option-def-detail', {
            parent: 'entity',
            url: '/coverage-term-option-def/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermOptionDef.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-option-def/coverage-term-option-def-detail.html',
                    controller: 'CoverageTermOptionDefDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermOptionDef');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoverageTermOptionDef', function($stateParams, CoverageTermOptionDef) {
                    return CoverageTermOptionDef.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage-term-option-def.new', {
            parent: 'coverage-term-option-def',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option-def/coverage-term-option-def-dialog.html',
                    controller: 'CoverageTermOptionDefDialogController',
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
                    $state.go('coverage-term-option-def', null, { reload: true });
                }, function() {
                    $state.go('coverage-term-option-def');
                });
            }]
        })
        .state('coverage-term-option-def.edit', {
            parent: 'coverage-term-option-def',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option-def/coverage-term-option-def-dialog.html',
                    controller: 'CoverageTermOptionDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoverageTermOptionDef', function(CoverageTermOptionDef) {
                            return CoverageTermOptionDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-option-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage-term-option-def.delete', {
            parent: 'coverage-term-option-def',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option-def/coverage-term-option-def-delete-dialog.html',
                    controller: 'CoverageTermOptionDefDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoverageTermOptionDef', function(CoverageTermOptionDef) {
                            return CoverageTermOptionDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-option-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
