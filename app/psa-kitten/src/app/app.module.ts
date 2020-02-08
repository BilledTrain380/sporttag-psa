/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import {APP_BASE_HREF} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {CoreModule} from './@core/core.module';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {ThemeModule} from './@theme/theme.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {HttpModule} from './modules/http/http.module';
import {PreviousRouteModule} from './modules/previous-route/previous-route.module';
import {AuthGuard, RoleGuard, RoleProvider} from './app.security';
import {NbRoleProvider, NbSecurityModule} from '@nebular/security';
import {NbAuthModule, NbAuthOAuth2JWTToken, NbOAuth2AuthStrategy, NbOAuth2ResponseType} from '@nebular/auth';
import {environment} from '../environments/environment';
import {LoginComponent} from './auth/login/login.component';
import {OauthcallbackComponent} from './auth/oauthcallback/oauthcallback.component';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {JSONTranslateLoader} from './@core/lang/translate.service';

@NgModule({
    declarations: [AppComponent, LoginComponent, OauthcallbackComponent],
    entryComponents: [LoginComponent, OauthcallbackComponent],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        AppRoutingModule,
        HttpModule,
        ThemeModule.forRoot(),
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useClass: JSONTranslateLoader,
            },
        }),
        PreviousRouteModule.forRoot(),
        NgbModule.forRoot(),
        CoreModule.forRoot(),
        NbAuthModule.forRoot({
            strategies: [
                NbOAuth2AuthStrategy.setup({
                    name: 'psa-dragon',
                    clientId: 'psa-kitten',
                    clientSecret: '',
                    authorize: {
                        endpoint: `${environment.devHost}/oauth/authorize`,
                        responseType: NbOAuth2ResponseType.TOKEN,
                        scope: [
                            'user',
                            'group_read',
                            'group_write',
                            'sport_read',
                            'discipline_read',
                            'competitor_read',
                            'competitor_write',
                            'participant_read',
                            'participant_write',
                            'participation',
                            'files',
                            'ranking',
                            'event_sheets',
                            'participant_list',
                        ].join(' '),
                        redirectUri: `${environment.devServer}${environment.baseHref}/auth/callback`,
                    },
                    redirect: {
                        success: 'pages/group/overview',
                    },
                    token: {
                        class: NbAuthOAuth2JWTToken,
                        grantType: 'implicit',
                    },
                }),
            ],
        }),
        NbSecurityModule.forRoot({
            accessControl: {
                ROLE_USER: {
                    view: ['groups', 'results', 'user'],
                    create: ['results'],
                },
                ROLE_ADMIN: {
                    view: '*',
                    create: '*',
                    remove: '*',
                },
            },
        }),
    ],
    bootstrap: [AppComponent],
    providers: [
        {provide: APP_BASE_HREF, useValue: environment.baseHref},
        {
            provide: NbRoleProvider,
            useClass: RoleProvider,
        },
        AuthGuard,
        RoleGuard,
    ],
})
export class AppModule {
}
