/**
 * @license
 * Copyright Akveo. All Rights Reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */
import {Component, OnInit} from '@angular/core';
import {AnalyticsService} from './@core/utils/analytics.service';
import {TranslateService} from '@ngx-translate/core';

@Component({
    selector: 'ngx-app',
    template: '<router-outlet></router-outlet>',
})
export class AppComponent implements OnInit {

    constructor(
        private readonly analytics: AnalyticsService,
        private readonly translate: TranslateService,
    ) {}

    ngOnInit(): void {
        this.analytics.trackPageViews();

        this.translate.setDefaultLang('lang_en');
        this.translate.use(`lang_${this.translate.getBrowserLang()}`);
    }
}
