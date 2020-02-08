import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';

import {PagesComponent} from './pages.component';
import {NotFoundComponent} from './miscellaneous/not-found/not-found.component';
import {GroupOverviewComponent} from './group/overview/group-overview.component';
import {GroupDetailComponent} from './group/detail/group-detail.component';
import {ManagementComponent} from './management/management.component';
import {AuthGuard, RoleGuard} from '../app.security';
import {EventPageComponent} from './event/event-page/event-page.component';

const routes: Routes = [{
    path: '',
    component: PagesComponent,
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard, RoleGuard],
    children: [{
        path: 'group/overview',
        component: GroupOverviewComponent,
    }, {
        path: 'group/detail/:name',
        component: GroupDetailComponent,
    }, {
        path: 'management',
        component: ManagementComponent,
    }, {
        path: 'athletics',
        loadChildren: './athletics/athletics-page.module#AthleticsPageModule',
    }, {
        path: 'settings',
        loadChildren: './settings/settings.module#SettingsModule',
    }, {
        path: 'event',
        component: EventPageComponent,
    }, {
        path: 'miscellaneous',
        loadChildren: './miscellaneous/miscellaneous.module#MiscellaneousModule',
    }, {
        path: '',
        redirectTo: 'group/overview',
        pathMatch: 'full',
    }, {
        path: '**',
        component: NotFoundComponent,
    }],
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class PagesRoutingModule {
}
