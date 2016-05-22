(function() {
    'use strict';

    angular
        .module('alotApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('personal-auto-vehicle', {
            parent: 'entity',
            url: '/personal-auto-vehicle',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.personalAutoVehicle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personal-auto-vehicle/personal-auto-vehicles.html',
                    controller: 'PersonalAutoVehicleController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personalAutoVehicle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('personal-auto-vehicle-detail', {
            parent: 'entity',
            url: '/personal-auto-vehicle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'alotApp.personalAutoVehicle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/personal-auto-vehicle/personal-auto-vehicle-detail.html',
                    controller: 'PersonalAutoVehicleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('personalAutoVehicle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PersonalAutoVehicle', function($stateParams, PersonalAutoVehicle) {
                    return PersonalAutoVehicle.get({id : $stateParams.id});
                }]
            }
        })
        .state('personal-auto-vehicle.new', {
            parent: 'personal-auto-vehicle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-auto-vehicle/personal-auto-vehicle-dialog.html',
                    controller: 'PersonalAutoVehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fixedId: null,
                                year: null,
                                make: null,
                                model: null,
                                bodyStyle: null,
                                symbols: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('personal-auto-vehicle', null, { reload: true });
                }, function() {
                    $state.go('personal-auto-vehicle');
                });
            }]
        })
        .state('personal-auto-vehicle.edit', {
            parent: 'personal-auto-vehicle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-auto-vehicle/personal-auto-vehicle-dialog.html',
                    controller: 'PersonalAutoVehicleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PersonalAutoVehicle', function(PersonalAutoVehicle) {
                            return PersonalAutoVehicle.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('personal-auto-vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('personal-auto-vehicle.delete', {
            parent: 'personal-auto-vehicle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/personal-auto-vehicle/personal-auto-vehicle-delete-dialog.html',
                    controller: 'PersonalAutoVehicleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PersonalAutoVehicle', function(PersonalAutoVehicle) {
                            return PersonalAutoVehicle.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('personal-auto-vehicle', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
