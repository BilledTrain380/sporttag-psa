import {Component} from '@angular/core';
import {NbDialogRef} from '@nebular/theme';

@Component({
    selector: 'ngx-confirmation',
    templateUrl: './confirmation.component.html',
    styleUrls: ['./confirmation.component.scss'],
})
export class ConfirmationComponent {

    message: string = '';
    severity: string = 'danger';

    constructor(
        private readonly ref: NbDialogRef<ConfirmationComponent>,
    ) {
    }

    dismissModal(): void {
        this.ref.close();
    }

    confirm(): void {
        this.ref.close(true);
    }
}
