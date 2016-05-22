(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('discount-def', {
            parent: 'entity',
            url: '/discount-def',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.discountDef.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-def/discount-defs.html',
                    controller: 'DiscountDefController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discountDef');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('discount-def-detail', {
            parent: 'entity',
            url: '/discount-def/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.discountDef.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount-def/discount-def-detail.html',
                    controller: 'DiscountDefDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discountDef');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DiscountDef', function($stateParams, DiscountDef) {
                    return DiscountDef.get({id : $stateParams.id});
                }]
            }
        })
        .state('discount-def.new', {
            parent: 'discount-def',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-def/discount-def-dialog.html',
                    controller: 'DiscountDefDialogController',
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
                    $state.go('discount-def', null, { reload: true });
                }, function() {
                    $state.go('discount-def');
                });
            }]
        })
        .state('discount-def.edit', {
            parent: 'discount-def',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-def/discount-def-dialog.html',
                    controller: 'DiscountDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscountDef', function(DiscountDef) {
                            return DiscountDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount-def.delete', {
            parent: 'discount-def',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount-def/discount-def-delete-dialog.html',
                    controller: 'DiscountDefDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DiscountDef', function(DiscountDef) {
                            return DiscountDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
