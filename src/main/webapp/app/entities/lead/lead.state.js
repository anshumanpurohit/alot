(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('lead', {
            parent: 'entity',
            url: '/lead',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.lead.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lead/leads.html',
                    controller: 'LeadController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lead');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('lead-detail', {
            parent: 'entity',
            url: '/lead/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.lead.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/lead/lead-detail.html',
                    controller: 'LeadDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('lead');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Lead', function($stateParams, Lead) {
                    return Lead.get({id : $stateParams.id});
                }]
            }
        })
        .state('lead.new', {
            parent: 'lead',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-dialog.html',
                    controller: 'LeadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                requestedTime: null,
                                responseTime: null,
                                premimumAmount: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('lead');
                });
            }]
        })
        .state('lead.edit', {
            parent: 'lead',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-dialog.html',
                    controller: 'LeadDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Lead', function(Lead) {
                            return Lead.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('lead.delete', {
            parent: 'lead',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/lead/lead-delete-dialog.html',
                    controller: 'LeadDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Lead', function(Lead) {
                            return Lead.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('lead', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
