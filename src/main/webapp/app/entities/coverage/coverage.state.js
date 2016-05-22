(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coverage', {
            parent: 'entity',
            url: '/coverage',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverage.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage/coverages.html',
                    controller: 'CoverageController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverage');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coverage-detail', {
            parent: 'entity',
            url: '/coverage/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.coverage.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coverage/coverage-detail.html',
                    controller: 'CoverageDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coverage');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Coverage', function($stateParams, Coverage) {
                    return Coverage.get({id : $stateParams.id});
                }]
            }
        })
        .state('coverage.new', {
            parent: 'coverage',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage/coverage-dialog.html',
                    controller: 'CoverageDialogController',
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
                    $state.go('coverage', null, { reload: true });
                }, function() {
                    $state.go('coverage');
                });
            }]
        })
        .state('coverage.edit', {
            parent: 'coverage',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage/coverage-dialog.html',
                    controller: 'CoverageDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Coverage', function(Coverage) {
                            return Coverage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coverage.delete', {
            parent: 'coverage',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coverage/coverage-delete-dialog.html',
                    controller: 'CoverageDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Coverage', function(Coverage) {
                            return Coverage.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('coverage', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
