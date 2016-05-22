(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('discount', {
            parent: 'entity',
            url: '/discount',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.discount.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount/discounts.html',
                    controller: 'DiscountController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discount');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('discount-detail', {
            parent: 'entity',
            url: '/discount/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.discount.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/discount/discount-detail.html',
                    controller: 'DiscountDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('discount');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Discount', function($stateParams, Discount) {
                    return Discount.get({id : $stateParams.id});
                }]
            }
        })
        .state('discount.new', {
            parent: 'discount',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-dialog.html',
                    controller: 'DiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                amount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: true });
                }, function() {
                    $state.go('discount');
                });
            }]
        })
        .state('discount.edit', {
            parent: 'discount',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-dialog.html',
                    controller: 'DiscountDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Discount', function(Discount) {
                            return Discount.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('discount.delete', {
            parent: 'discount',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/discount/discount-delete-dialog.html',
                    controller: 'DiscountDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Discount', function(Discount) {
                            return Discount.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('discount', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
