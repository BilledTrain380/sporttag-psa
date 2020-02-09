import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbCollapseModule, NgbDropdownModule, NgbNavModule } from "@ng-bootstrap/ng-bootstrap";

import { CoreModule } from "../@core/core.module";

import { FooterComponent } from "./footer/footer.component";
import { HeaderComponent } from "./header/header.component";
import { LayoutButtonsComponent } from "./layout/layout-buttons/layout-buttons.component";
import { PageHeaderComponent } from "./layout/page-header/page-header.component";
import { SimplePageComponent } from "./layout/simple-page/simple-page.component";
import { SidebarComponent } from "./sidebar/sidebar.component";

@NgModule({
  declarations: [
    HeaderComponent,
    SidebarComponent,
    PageHeaderComponent,
    SimplePageComponent,
    LayoutButtonsComponent,
    FooterComponent,
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
    FooterComponent,
    SidebarComponent,
    SimplePageComponent,
    LayoutButtonsComponent,
  ],
})
export class ThemeModule {
}
