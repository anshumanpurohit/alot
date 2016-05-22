(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-line', {
            parent: 'entity',
            url: '/product-line',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.productLine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-line/product-lines.html',
                    controller: 'ProductLineController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productLine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-line-detail', {
            parent: 'entity',
            url: '/product-line/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.productLine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product-line/product-line-detail.html',
                    controller: 'ProductLineDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('productLine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProductLine', function($stateParams, ProductLine) {
                    return ProductLine.get({id : $stateParams.id});
                }]
            }
        })
        .state('product-line.new', {
            parent: 'product-line',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line/product-line-dialog.html',
                    controller: 'ProductLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startEffectiveDate: null,
                                endEffectiveDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('product-line', null, { reload: true });
                }, function() {
                    $state.go('product-line');
                });
            }]
        })
        .state('product-line.edit', {
            parent: 'product-line',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line/product-line-dialog.html',
                    controller: 'ProductLineDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProductLine', function(ProductLine) {
                            return ProductLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-line.delete', {
            parent: 'product-line',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product-line/product-line-delete-dialog.html',
                    controller: 'ProductLineDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProductLine', function(ProductLine) {
                            return ProductLine.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-line', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
