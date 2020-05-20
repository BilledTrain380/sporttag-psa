import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbAlertModule, NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";
import { AlertComponent } from './component/alert/alert.component';

import { CardComponent } from "./component/card/card.component";
import { StatusDetailComponent } from "./component/status/status-detail/status-detail.component";
import { StatusComponent } from "./component/status/status.component";
import { MenuItemComponent } from "./menu/menu-item/menu-item.component";

@NgModule({
            declarations: [
              MenuItemComponent,
              CardComponent,
              StatusComponent,
              StatusDetailComponent,
              AlertComponent,
            ],
            imports: [
              CommonModule,
              RouterModule,
              NgbPopoverModule,
              NgbAlertModule,
              FontAwesomeModule,
            ],
            exports: [
              MenuItemComponent,
              CardComponent,
              StatusComponent,
              AlertComponent,
            ],
          })
export class CoreModule {
}
