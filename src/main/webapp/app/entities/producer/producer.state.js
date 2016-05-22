(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('producer', {
            parent: 'entity',
            url: '/producer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.producer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/producer/producers.html',
                    controller: 'ProducerController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('producer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('producer-detail', {
            parent: 'entity',
            url: '/producer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.producer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/producer/producer-detail.html',
                    controller: 'ProducerDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('producer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Producer', function($stateParams, Producer) {
                    return Producer.get({id : $stateParams.id});
                }]
            }
        })
        .state('producer.new', {
            parent: 'producer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/producer/producer-dialog.html',
                    controller: 'ProducerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                deleted: null,
                                externalReferenceId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('producer', null, { reload: true });
                }, function() {
                    $state.go('producer');
                });
            }]
        })
        .state('producer.edit', {
            parent: 'producer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/producer/producer-dialog.html',
                    controller: 'ProducerDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Producer', function(Producer) {
                            return Producer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('producer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('producer.delete', {
            parent: 'producer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/producer/producer-delete-dialog.html',
                    controller: 'ProducerDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Producer', function(Producer) {
                            return Producer.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('producer', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
