import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { IfAuthorityDirective } from "./if-authority.directive";

const COMPONENTS = [
  IfAuthorityDirective,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
            ],
            exports: [...COMPONENTS],
          })
export class SecurityModule {
}
