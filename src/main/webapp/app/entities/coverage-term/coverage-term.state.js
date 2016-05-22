(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage-term', {
            parent: 'entity',
            url: '/coverage-term',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTerm.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term/coverage-terms.html',
                    controller: 'CoverageTermController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTerm');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-term-detail', {
            parent: 'entity',
            url: '/coverage-term/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverageTerm.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage-term/coverage-term-detail.html',
                    controller: 'CoverageTermDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverageTerm');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CoverageTerm', function($stateParams, CoverageTerm) {
                    return CoverageTerm.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage-term.new', {
            parent: 'coverage-term',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term/coverage-term-dialog.html',
                    controller: 'CoverageTermDialogController',
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
                    $state.go('coverage-term', null, { reload: true });
                }, function() {
                    $state.go('coverage-term');
                });
            }]
        })
        .state('coverage-term.edit', {
            parent: 'coverage-term',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term/coverage-term-dialog.html',
                    controller: 'CoverageTermDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CoverageTerm', function(CoverageTerm) {
                            return CoverageTerm.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage-term.delete', {
            parent: 'coverage-term',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage-term/coverage-term-delete-dialog.html',
                    controller: 'CoverageTermDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CoverageTerm', function(CoverageTerm) {
                            return CoverageTerm.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage-term', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
