import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {GROUP_PROVIDER, HttpGroupProvider} from './group-providers';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
    providers: [
        {
            provide: GROUP_PROVIDER,
            useClass: HttpGroupProvider,
        },
    ],
})
export class GroupModule {
}
