import {Component, Inject} from '@angular/core';
import {GROUP_PROVIDER, GroupProvider} from '../../../../modules/group/group-providers';
import {BadRequestError} from '../../../../modules/http/http-errors';
import {NbDialogRef} from '@nebular/theme';

@Component({
    selector: 'ngx-import',
    templateUrl: './import.component.html',
    styleUrls: ['./import.component.scss'],
})
export class ImportComponent {

    errorMessage?: string;

    loading: boolean = false;

    file?: File;

    constructor(
        private readonly ref: NbDialogRef<ImportComponent>,

        @Inject(GROUP_PROVIDER)
        private readonly groupProvider: GroupProvider,
    ) {
    }

    setFile(file: File): void {
        this.file = file;
    }

    async upload(): Promise<void> {

        try {

            this.loading = true;

            if (this.file !== undefined) {
                await this.groupProvider.import(this.file);
                this.ref.close(true);
            }

        } catch (error) {

            if (error instanceof BadRequestError) {
                this.errorMessage = error.message;
            }

        } finally {
            this.loading = false;
        }
    }

    dismissModal(): void {
        this.ref.close();
    }
}
