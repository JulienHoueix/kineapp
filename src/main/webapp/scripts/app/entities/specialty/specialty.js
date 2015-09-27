'use strict';

angular.module('kineappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('specialty', {
                parent: 'entity',
                url: '/specialties',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'kineappApp.specialty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specialty/specialties.html',
                        controller: 'SpecialtyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specialty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('specialty.detail', {
                parent: 'entity',
                url: '/specialty/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'kineappApp.specialty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/specialty/specialty-detail.html',
                        controller: 'SpecialtyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('specialty');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Specialty', function($stateParams, Specialty) {
                        return Specialty.get({id : $stateParams.id});
                    }]
                }
            })
            .state('specialty.new', {
                parent: 'specialty',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/specialty/specialty-dialog.html',
                        controller: 'SpecialtyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('specialty', null, { reload: true });
                    }, function() {
                        $state.go('specialty');
                    })
                }]
            })
            .state('specialty.edit', {
                parent: 'specialty',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/specialty/specialty-dialog.html',
                        controller: 'SpecialtyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Specialty', function(Specialty) {
                                return Specialty.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('specialty', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
