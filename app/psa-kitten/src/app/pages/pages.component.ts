import {Component, OnDestroy, OnInit} from '@angular/core';

import {MENU_ITEMS} from './pages-menu';
import {NbAccessChecker} from '@nebular/security';
import {takeWhile} from 'rxjs/operators';
import {NbMenuItem} from '@nebular/theme';
import {TranslateService} from '@ngx-translate/core';
import {Observable} from 'rxjs';

@Component({
  selector: 'ngx-pages',
  styleUrls: ['pages.component.scss'],
  template: `
    <ngx-sample-layout>
      <nb-menu [items]="menu"></nb-menu>
      <router-outlet></router-outlet>
    </ngx-sample-layout>
  `,
})
export class PagesComponent implements OnInit, OnDestroy {

    readonly menu = MENU_ITEMS;

    private alive: boolean = true;

    constructor(
        private readonly translate: TranslateService,
        private readonly accessChecker: NbAccessChecker,
    ) {}

    ngOnInit(): void {

        this.menu.forEach(it => {
            this.translateMenuItem(it);
            this.checkMenu(it);
        });
    }

    private translateMenuItem(item: NbMenuItem): void {

        if (item.data && item.data.translation) {

            this.translate.get(item.data.translation).subscribe(it => {
                item.title = it;
            });
        }

        if (item.children) {
            item.children.forEach(it => this.translateMenuItem(it));
        }
    }

    private checkMenu(menu: NbMenuItem): void {

        if (menu.data && menu.data.canShow) {

            const canShowObservable: Observable<boolean> = menu.data.canShow(this.accessChecker);

            canShowObservable
                .pipe(takeWhile(() => this.alive))
                .forEach(canShow => {
                    menu.hidden = !canShow;
                });
        }

        if (menu.children)
            menu.children.forEach(it => this.checkMenu(it));
    }


    ngOnDestroy(): void {
        this.alive = false;
    }
}
