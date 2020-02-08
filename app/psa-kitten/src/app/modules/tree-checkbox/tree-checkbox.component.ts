import {Component, Input, OnDestroy} from '@angular/core';
import {TreeCheckbox} from './tree-checkbox-models';
import {NbLayoutDirection, NbLayoutDirectionService} from '@nebular/theme';
import {takeWhile} from 'rxjs/operators';

@Component({
    selector: 'ngx-tree-checkbox',
    templateUrl: './tree-checkbox.component.html',
    styleUrls: ['./tree-checkbox.component.scss'],
})
export class TreeCheckboxComponent implements OnDestroy {

    @Input('root')
    readonly root: TreeCheckbox = new TreeCheckbox('');

    currentDirection: NbLayoutDirection;

    private alive: boolean = true;

    constructor(
        private directionService: NbLayoutDirectionService,
    ) {
        this.currentDirection = this.directionService.getDirection();

        this.directionService.onDirectionChange()
            .pipe(takeWhile(() => this.alive))
            .subscribe(newDirection => this.currentDirection = newDirection);
    }

    ngOnDestroy(): void {
        this.alive = false;
    }
}
