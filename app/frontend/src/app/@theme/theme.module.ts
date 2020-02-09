import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { NgbDropdownModule } from "@ng-bootstrap/ng-bootstrap";

import { HeaderComponent } from "./header/header.component";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";

@NgModule({
  declarations: [HeaderComponent],
  imports: [
    CommonModule,
    NgbDropdownModule,
    FontAwesomeModule,
  ],
  exports: [HeaderComponent],
})
export class ThemeModule {
}
