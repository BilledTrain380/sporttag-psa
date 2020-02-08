import {Component, Input} from '@angular/core';

@Component({
    selector: 'ngx-page-title',
    templateUrl: './page-title.component.html',
    styleUrls: ['./page-title.component.scss'],
})
export class PageTitleComponent {

    @Input('title')
    readonly title: string = '';

    @Input('subtitle')
    readonly subtitle: string = '';
}
