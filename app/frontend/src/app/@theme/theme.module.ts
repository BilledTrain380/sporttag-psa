import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbCollapseModule, NgbDropdownModule, NgbNavModule } from "@ng-bootstrap/ng-bootstrap";

import { HeaderComponent } from "./header/header.component";
import { SidebarComponent } from "./sidebar/sidebar.component";
import { CoreModule } from "../@core/core.module";

@NgModule({
  declarations: [
    HeaderComponent,
    SidebarComponent,
  ],
  imports: [
    CommonModule,
    CoreModule,
    NgbDropdownModule,
    NgbNavModule,
    NgbCollapseModule,
    RouterModule,
    FontAwesomeModule,
  ],
  exports: [
    HeaderComponent,
    SidebarComponent,
  ],
})
export class ThemeModule {
}
