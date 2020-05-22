import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { RouterModule } from "@angular/router";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { NgbCollapseModule, NgbDropdownModule, NgbNavModule } from "@ng-bootstrap/ng-bootstrap";

import { CoreModule } from "../@core/core.module";

import { FooterComponent } from "./footer/footer.component";
import { HeaderComponent } from "./header/header.component";
import { CardComponent } from "./layout/card/card.component";
import { ColumnLayoutComponent } from "./layout/column-layout/column-layout.component";
import { LayoutButtonComponent } from "./layout/layout-button/layout-button.component";
import { LayoutButtonsComponent } from "./layout/layout-buttons/layout-buttons.component";
import { PageHeaderComponent } from "./layout/page-header/page-header.component";
import { RowLayoutComponent } from "./layout/row-layout/row-layout.component";
import { RowPageComponent } from "./layout/row-page/row-page.component";
import { SimplePageComponent } from "./layout/simple-page/simple-page.component";
import { SidebarComponent } from "./sidebar/sidebar.component";

const BASE_COMPONENTS = [
  HeaderComponent,
  FooterComponent,
  SidebarComponent,
  CardComponent,
  SimplePageComponent,
  RowPageComponent,
  RowLayoutComponent,
  ColumnLayoutComponent,
  LayoutButtonsComponent,
  LayoutButtonComponent,
];

const MODULES = [
  CommonModule,
  FontAwesomeModule,
  ReactiveFormsModule,
];

@NgModule({
            declarations: [
              ...BASE_COMPONENTS,
              PageHeaderComponent,
            ],
            imports: [
              ...MODULES,
              CoreModule,
              RouterModule,
              NgbNavModule,
              NgbDropdownModule,
              NgbCollapseModule,
            ],
            exports: [...BASE_COMPONENTS, ...MODULES],
          })
export class ThemeModule {
}
