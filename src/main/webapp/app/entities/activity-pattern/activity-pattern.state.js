(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('activity-pattern', {
            parent: 'entity',
            url: '/activity-pattern',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.activityPattern.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-pattern/activity-patterns.html',
                    controller: 'ActivityPatternController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityPattern');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('activity-pattern-detail', {
            parent: 'entity',
            url: '/activity-pattern/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.activityPattern.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/activity-pattern/activity-pattern-detail.html',
                    controller: 'ActivityPatternDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('activityPattern');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ActivityPattern', function($stateParams, ActivityPattern) {
                    return ActivityPattern.get({id : $stateParams.id});
                }]
            }
        })
        .state('activity-pattern.new', {
            parent: 'activity-pattern',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-pattern/activity-pattern-dialog.html',
                    controller: 'ActivityPatternDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                deleted: null,
                                description: null,
                                escalationDays: null,
                                createActivityFor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('activity-pattern', null, { reload: true });
                }, function() {
                    $state.go('activity-pattern');
                });
            }]
        })
        .state('activity-pattern.edit', {
            parent: 'activity-pattern',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-pattern/activity-pattern-dialog.html',
                    controller: 'ActivityPatternDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ActivityPattern', function(ActivityPattern) {
                            return ActivityPattern.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-pattern', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('activity-pattern.delete', {
            parent: 'activity-pattern',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/activity-pattern/activity-pattern-delete-dialog.html',
                    controller: 'ActivityPatternDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ActivityPattern', function(ActivityPattern) {
                            return ActivityPattern.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('activity-pattern', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
