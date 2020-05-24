import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbAlertModule, NgbPopoverModule } from "@ng-bootstrap/ng-bootstrap";

@NgModule({
            declarations: [],
            imports: [
              CommonModule,
              RouterModule,
              NgbPopoverModule,
              NgbAlertModule,
              FontAwesomeModule,
            ],
            exports: [],
          })
export class CoreModule {
}
