'use strict';

angular.module('kineappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('physiotherapist', {
                parent: 'entity',
                url: '/physiotherapists',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'kineappApp.physiotherapist.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/physiotherapist/physiotherapists.html',
                        controller: 'PhysiotherapistController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('physiotherapist');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('physiotherapist.detail', {
                parent: 'entity',
                url: '/physiotherapist/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'kineappApp.physiotherapist.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/physiotherapist/physiotherapist-detail.html',
                        controller: 'PhysiotherapistDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('physiotherapist');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Physiotherapist', function($stateParams, Physiotherapist) {
                        return Physiotherapist.get({id : $stateParams.id});
                    }]
                }
            })
            .state('physiotherapist.new', {
                parent: 'physiotherapist',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/physiotherapist/physiotherapist-dialog.html',
                        controller: 'PhysiotherapistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {firstName: null, lastName: null, street: null, postalCode: null, city: null, country: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('physiotherapist', null, { reload: true });
                    }, function() {
                        $state.go('physiotherapist');
                    })
                }]
            })
            .state('physiotherapist.edit', {
                parent: 'physiotherapist',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/physiotherapist/physiotherapist-dialog.html',
                        controller: 'PhysiotherapistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Physiotherapist', function(Physiotherapist) {
                                return Physiotherapist.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('physiotherapist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
