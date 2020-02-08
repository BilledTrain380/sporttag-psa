import {Component, Inject, Input, OnDestroy, OnInit} from '@angular/core';
import {NbDialogService, NbMenuItem, NbMenuService, NbSidebarService} from '@nebular/theme';
import {AnalyticsService} from '../../../@core/utils/analytics.service';
import {filter, takeWhile} from 'rxjs/operators';
import {User} from '../../../modules/user/user-models';
import {ChangePasswordComponent} from '../change-password/change-password.component';
import {USER_SUPPLIER, UserSupplier} from '../../../modules/user/user-providers';
import {NbTokenService} from '@nebular/auth';
import {environment} from '../../../../environments/environment';
import {TranslateService} from '@ngx-translate/core';
import {LayoutService} from '../../../@core/data/layout.service';

@Component({
    selector: 'ngx-header',
    styleUrls: ['./header.component.scss'],
    templateUrl: './header.component.html',
})
export class HeaderComponent implements OnInit, OnDestroy {

    private alive: boolean = true;

    @Input() position = 'normal';

    user: User;

    userMenu = [
        {
            title: 'Change Password',
            data: {
                // HeaderComponent will be bind to this
                click: function () {

                    this.dialogService.open(ChangePasswordComponent, {
                        context: {
                            user: this.user,
                        },
                    });
                },
                translation: 'label.changePassword',
            },
        },
        {
            title: 'Log out',
            data: {
                // HeaderComponent will be bind to this
                click: function () {
                    // Logout should be done through NbAuthService.logout(), but it does not seem to work.
                    this.tokenService.clear();
                    window.open(`${environment.host}/logout`, '_self');
                },
                translation: 'label.logout',
            },
        },
    ];

    constructor(
        private readonly sidebarService: NbSidebarService,
        private readonly menuService: NbMenuService,
        private readonly tokenService: NbTokenService, // tslint:disable-line: no-unused-variable
        @Inject(USER_SUPPLIER) private readonly userSupplier: UserSupplier,
        private readonly translateService: TranslateService,
        private readonly analyticsService: AnalyticsService,
        private readonly dialogService: NbDialogService, // tslint:disable-line: no-unused-variable
        private readonly layoutService: LayoutService,
    ) {
    }

    ngOnInit() {

        this.userSupplier.getActiveUser()
            .pipe(takeWhile(() => this.alive))
            .forEach(user => {
                this.user = user;
            });

        this.menuService.onItemClick()
            .pipe(filter(({ tag }) => tag === 'user-context-menu'))
            .subscribe(({ item }) => {
                if (item.data && item.data.click) item.data.click.apply(this);
            });

        this.userMenu.forEach(it => this.translateMenuItem(it));
    }

    ngOnDestroy(): void {
        this.alive = false;
    }

  toggleSidebar(): boolean {
    this.sidebarService.toggle(true, 'menu-sidebar');
    this.layoutService.changeLayoutSize();

    return false;
  }

  toggleSettings(): boolean {
    this.sidebarService.toggle(false, 'settings-sidebar');

    return false;
  }

    goToHome() {
        this.menuService.navigateHome();
    }

    startSearch() {
        this.analyticsService.trackEvent('startSearch');
    }

    private translateMenuItem(item: NbMenuItem): void {

        if (item.data && item.data.translation) {

            this.translateService.get(item.data.translation).subscribe(it => {
                item.title = it;
            });
        }

        if (item.children) {
            item.children.forEach(it => this.translateMenuItem(it));
        }
    }
}
