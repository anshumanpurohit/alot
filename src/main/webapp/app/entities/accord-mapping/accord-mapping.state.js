(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('accord-mapping', {
            parent: 'entity',
            url: '/accord-mapping',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.accordMapping.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accord-mapping/accord-mappings.html',
                    controller: 'AccordMappingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accordMapping');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('accord-mapping-detail', {
            parent: 'entity',
            url: '/accord-mapping/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.accordMapping.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accord-mapping/accord-mapping-detail.html',
                    controller: 'AccordMappingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accordMapping');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AccordMapping', function($stateParams, AccordMapping) {
                    return AccordMapping.get({id : $stateParams.id});
                }]
            }
        })
        .state('accord-mapping.new', {
            parent: 'accord-mapping',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accord-mapping/accord-mapping-dialog.html',
                    controller: 'AccordMappingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                input: null,
                                output: null,
                                accrodVersion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('accord-mapping', null, { reload: true });
                }, function() {
                    $state.go('accord-mapping');
                });
            }]
        })
        .state('accord-mapping.edit', {
            parent: 'accord-mapping',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accord-mapping/accord-mapping-dialog.html',
                    controller: 'AccordMappingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AccordMapping', function(AccordMapping) {
                            return AccordMapping.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('accord-mapping', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accord-mapping.delete', {
            parent: 'accord-mapping',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accord-mapping/accord-mapping-delete-dialog.html',
                    controller: 'AccordMappingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AccordMapping', function(AccordMapping) {
                            return AccordMapping.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('accord-mapping', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
