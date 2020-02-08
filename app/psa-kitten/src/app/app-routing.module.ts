import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {NbAuthComponent} from '@nebular/auth';
import {LoginComponent} from './auth/login/login.component';
import {OauthcallbackComponent} from './auth/oauthcallback/oauthcallback.component';

const routes: Routes = [
    {path: 'pages', loadChildren: 'app/pages/pages.module#PagesModule'},
    {
        path: 'auth',
        component: NbAuthComponent,
        children: [
            {
                path: '',
                component: LoginComponent,
            },
            {
                path: 'login',
                component: LoginComponent,
                pathMatch: 'full',
            },
            {
                path: 'callback',
                component: OauthcallbackComponent,
            },
            // This may be needed in the future
            // {
            //     path: 'logout',
            //     component: NbLogoutComponent,
            // },
            // {
            //     path: 'request-password',
            //     component: NbRequestPasswordComponent,
            // },
            // {
            //     path: 'reset-password',
            //     component: NbResetPasswordComponent,
            // },
        ],
    },
    {path: '', redirectTo: 'pages', pathMatch: 'full'},
    {path: '**', redirectTo: 'pages'},
];

const config: ExtraOptions = {
};

@NgModule({
    imports: [RouterModule.forRoot(routes, config)],
    exports: [RouterModule],
})
export class AppRoutingModule {
}
