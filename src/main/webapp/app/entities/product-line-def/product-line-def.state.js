(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-line-def', {
            parent: 'entity',
            url: '/product-line-def',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.productLineDef.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-line-def/product-line-defs.html',
                    controller: 'ProductLineDefController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productLineDef');
                    $translatePartialLoader.addPart('productLineType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-line-def-detail', {
            parent: 'entity',
            url: '/product-line-def/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.productLineDef.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-line-def/product-line-def-detail.html',
                    controller: 'ProductLineDefDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productLineDef');
                    $translatePartialLoader.addPart('productLineType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductLineDef', function($stateParams, ProductLineDef) {
                    return ProductLineDef.get({id : $stateParams.id});
                }]
            }
        })
        .state('product-line-def.new', {
            parent: 'product-line-def',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line-def/product-line-def-dialog.html',
                    controller: 'ProductLineDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                productLineType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-line-def', null, { reload: true });
                }, function() {
                    $state.go('product-line-def');
                });
            }]
        })
        .state('product-line-def.edit', {
            parent: 'product-line-def',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line-def/product-line-def-dialog.html',
                    controller: 'ProductLineDefDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductLineDef', function(ProductLineDef) {
                            return ProductLineDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-line-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-line-def.delete', {
            parent: 'product-line-def',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line-def/product-line-def-delete-dialog.html',
                    controller: 'ProductLineDefDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductLineDef', function(ProductLineDef) {
                            return ProductLineDef.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-line-def', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
