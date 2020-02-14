import { HttpClientModule } from "@angular/common/http";
import { NgModule } from "@angular/core";
import { BrowserModule } from "@angular/platform-browser";
import { NoopAnimationsModule } from "@angular/platform-browser/animations";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { StoreModule } from "@ngrx/store";
import { StoreDevtoolsModule } from "@ngrx/store-devtools";
import { OAuthModule } from "angular-oauth2-oidc";
import { LoggerModule, NgxLoggerLevel } from "ngx-logger";

import { environment } from "../environments/environment";

import { CoreModule } from "./@core/core.module";
import { ThemeModule } from "./@theme/theme.module";
import { AppRoutingModule } from "./app-routing.module";
import { AppComponent } from "./app.component";
import { metaReducers, reducers } from "./store";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NoopAnimationsModule,
    CoreModule,
    ThemeModule,
    FontAwesomeModule,
    HttpClientModule,
    OAuthModule.forRoot(),
    LoggerModule.forRoot({
      level: NgxLoggerLevel.INFO,
    }),
    StoreModule.forRoot(reducers, {
      metaReducers,
      runtimeChecks: {
        strictStateImmutability: true,
        strictActionImmutability: true,
      },
    }),
    !environment.production ? StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: environment.production,
    }) : [],
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {
}
