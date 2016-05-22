(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('violation', {
            parent: 'entity',
            url: '/violation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.violation.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/violation/violations.html',
                    controller: 'ViolationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('violation');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('violation-detail', {
            parent: 'entity',
            url: '/violation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.violation.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/violation/violation-detail.html',
                    controller: 'ViolationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('violation');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Violation', function($stateParams, Violation) {
                    return Violation.get({id : $stateParams.id});
                }]
            }
        })
        .state('violation.new', {
            parent: 'violation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/violation/violation-dialog.html',
                    controller: 'ViolationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                externalViolationCode: null,
                                description: null,
                                violationOccurredDate: null,
                                additionalDetails: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('violation', null, { reload: true });
                }, function() {
                    $state.go('violation');
                });
            }]
        })
        .state('violation.edit', {
            parent: 'violation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/violation/violation-dialog.html',
                    controller: 'ViolationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Violation', function(Violation) {
                            return Violation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('violation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('violation.delete', {
            parent: 'violation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/violation/violation-delete-dialog.html',
                    controller: 'ViolationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Violation', function(Violation) {
                            return Violation.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('violation', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
