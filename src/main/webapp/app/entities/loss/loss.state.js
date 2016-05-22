(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('loss', {
            parent: 'entity',
            url: '/loss',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.loss.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/loss/losses.html',
                    controller: 'LossController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loss');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('loss-detail', {
            parent: 'entity',
            url: '/loss/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.loss.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/loss/loss-detail.html',
                    controller: 'LossDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('loss');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Loss', function($stateParams, Loss) {
                    return Loss.get({id : $stateParams.id});
                }]
            }
        })
        .state('loss.new', {
            parent: 'loss',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/loss/loss-dialog.html',
                    controller: 'LossDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                externalRefId: null,
                                description: null,
                                lossOccurredDate: null,
                                claimAmount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('loss', null, { reload: true });
                }, function() {
                    $state.go('loss');
                });
            }]
        })
        .state('loss.edit', {
            parent: 'loss',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/loss/loss-dialog.html',
                    controller: 'LossDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Loss', function(Loss) {
                            return Loss.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('loss', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('loss.delete', {
            parent: 'loss',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/loss/loss-delete-dialog.html',
                    controller: 'LossDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Loss', function(Loss) {
                            return Loss.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('loss', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
