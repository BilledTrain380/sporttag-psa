import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { NoEntriesDirective } from "./no-entries.directive";

const COMPONENTS = [
  NoEntriesDirective,
];

@NgModule({
            declarations: [...COMPONENTS],
            imports: [
              CommonModule,
            ],
            exports: [...COMPONENTS],
          })
export class TableModule {
}
