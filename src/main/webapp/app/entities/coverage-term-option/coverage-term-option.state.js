(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage-term-option', {
            parent: 'entity',
            url: '/coverage-term-option',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermOption.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-option/coverage-term-options.html',
                    controller: 'CoverageTermOptionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermOption');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-term-option-detail', {
            parent: 'entity',
            url: '/coverage-term-option/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTermOption.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term-option/coverage-term-option-detail.html',
                    controller: 'CoverageTermOptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTermOption');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoverageTermOption', function($stateParams, CoverageTermOption) {
                    return CoverageTermOption.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage-term-option.new', {
            parent: 'coverage-term-option',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option/coverage-term-option-dialog.html',
                    controller: 'CoverageTermOptionDialogController',
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
                    $state.go('coverage-term-option', null, { reload: true });
                }, function() {
                    $state.go('coverage-term-option');
                });
            }]
        })
        .state('coverage-term-option.edit', {
            parent: 'coverage-term-option',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option/coverage-term-option-dialog.html',
                    controller: 'CoverageTermOptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoverageTermOption', function(CoverageTermOption) {
                            return CoverageTermOption.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-option', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage-term-option.delete', {
            parent: 'coverage-term-option',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term-option/coverage-term-option-delete-dialog.html',
                    controller: 'CoverageTermOptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoverageTermOption', function(CoverageTermOption) {
                            return CoverageTermOption.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term-option', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
