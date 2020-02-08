import {merge, Observable, Subject} from 'rxjs';

export class TreeCheckbox {

    readonly onChange: Observable<boolean | undefined>;

    private onChangeSubject: Subject<boolean | undefined> = new Subject();

    constructor(
        readonly label: string,
        readonly children: ReadonlyArray<TreeCheckbox> = [],
        readonly columnClass: string = 'col-lg-12',
        readonly data?: any,
        public checked: boolean | undefined = false,
        public collapsed: boolean = true,
    ) {
        this.onChange = this.onChangeSubject.asObservable();

        merge(...this.children.map(it => it.onChange)).subscribe(_ => {

            if (this.children.every(child => child.checked === true)) {

                this.checked = true;
                this.notifyParent(this.checked);
                this.selectInput().indeterminate = false;

            } else if (this.children.some(child => child.checked === true || child.checked === undefined)) {

                this.checked = undefined;
                this.notifyParent(this.checked);
                this.selectInput().indeterminate = true;

            } else {

                this.checked = false;
                this.notifyParent(this.checked);
                this.selectInput().indeterminate = false;
            }
        });

    }

    toggleCollapse(): void {
        this.collapsed = !this.collapsed;
    }

    triggerChange(value: boolean): void {
        this.changeRecursive(value);
        this.notifyParent(value);
    }

    changeRecursive(value: boolean): void {
        this.checked = value;
        this.selectInput().indeterminate = false;
        this.children.forEach(it => (it.changeRecursive(value)));
    }

    private selectInput(): HTMLInputElement {
        return document.getElementById(`tree-checkbox-${this.label}`) as HTMLInputElement;
    }

    private notifyParent(value: boolean): void {
        this.onChangeSubject.next(value);
    }
}
