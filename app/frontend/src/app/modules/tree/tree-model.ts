import { BehaviorSubject, merge, Observable, Subject } from "rxjs";
import { filter, map, takeUntil } from "rxjs/operators";

import { COLUMN_SIZE_12 } from "../../@theme/theme-constants";
import { buildColumnCssClass } from "../../@theme/utils/css-classes-utils";

export const LABEL_ALL = $localize`All`;

export class TreeBuilder {
  private label = "";
  private value = "";
  private isCollapsedEnabled = true;
  private isCollapsed = false;
  private splitting = 1;
  private readonly nodes: Array<TreeBuilder> = [];

  private constructor() {
  }

  static newBuilder(): TreeBuilder {
    return new TreeBuilder();
  }

  setLabel(label: string): TreeBuilder {
    this.label = label;

    if (!this.value) {
      this.value = label;
    }

    return this;
  }

  setValue(value: string): TreeBuilder {
    this.value = value;

    return this;
  }

  setCollapsed(isCollapsed: boolean): TreeBuilder {
    this.isCollapsed = isCollapsed;

    return this;
  }

  setCollapsedEnabled(isCollapsedEnabled: boolean): TreeBuilder {
    this.isCollapsedEnabled = isCollapsedEnabled;

    return this;
  }

  setSplitting(splitting: number): TreeBuilder {
    this.splitting = splitting;

    return this;
  }

  splitHalf(): TreeBuilder {
    // tslint:disable-next-line:no-magic-numbers
    return this.setSplitting(2);
  }

  addLeafNode(label: string, value: string = label): TreeBuilder {
    const node = TreeBuilder.newBuilder()
      .setLabel(label)
      .setValue(value);
    this.nodes.push(node);

    return this;
  }

  addLeafNodes(...labels: Array<string>): TreeBuilder {
    labels.forEach(label => this.addLeafNode(label));

    return this;
  }

  addNode(node: TreeBuilder): TreeBuilder {
    this.nodes.push(node);

    return this;
  }

  addNodes(nodes: ReadonlyArray<TreeBuilder>): TreeBuilder {
    this.nodes.push(...nodes);

    return this;
  }

  build(): TreeCheckNodeModel {
    const chunkSize = Math.max(Math.floor(this.nodes.length / this.splitting), 1);
    const nodeChildren = this.nodes.map(node => node.build());
    const nodeChunks: Array<ReadonlyArray<TreeCheckNodeModel>> = [];
    for (let i = 0; i < nodeChildren.length; i += chunkSize) {
      const chunk = nodeChildren.slice(i, i + chunkSize);
      nodeChunks.push(chunk);
    }

    return new TreeCheckNodeModel(this.label, this.value, this.isCollapsed, this.isCollapsedEnabled, this.splitting, nodeChunks);
  }
}

export class TreeCheckNodeModel {
  readonly columnCssClass: string;

  get isChecked(): boolean | undefined {
    return this.checkedChangeSubject$.value.isChecked;
  }

  set isChecked(value: boolean | undefined) {
    this.checkedChangeSubject$.next(TreeCheckNodeModelValue.of(value));
    this.flatNodes.forEach(node => node.setValueSilenced(value));
  }

  get isCollapsable(): boolean {
    return this.nodes.length > 0 && this.isCollapsedEnabled;
  }

  get checkedNodes(): ReadonlyArray<TreeCheckNodeModel> {
    return this.flatNodes.filter(node => node.isChecked);
  }

  readonly flatNodes: ReadonlyArray<TreeCheckNodeModel>;

  readonly isChecked$: Observable<boolean | undefined>;
  private readonly valueChanged$: Observable<TreeCheckNodeModelValue>;

  private readonly checkedChangeSubject$ = new BehaviorSubject(TreeCheckNodeModelValue.unChecked());
  private readonly close$ = new Subject();

  constructor(
    readonly label: string,
    readonly value: string,
    public isCollapsed: boolean,
    readonly isCollapsedEnabled: boolean,
    readonly splitter: number = 1,
    readonly nodes: ReadonlyArray<ReadonlyArray<TreeCheckNodeModel>> = [],
  ) {
    const columnSize = Math.floor(COLUMN_SIZE_12 / splitter);
    this.columnCssClass = buildColumnCssClass(columnSize);
    this.flatNodes = nodes.reduce((accumulator, val) => accumulator.concat(val), []);
    this.isChecked$ = this.checkedChangeSubject$.asObservable()
      .pipe(map(val => val.isChecked));
    this.valueChanged$ = this.checkedChangeSubject$.asObservable();

    merge(...this.flatNodes.map(node => node.valueChanged$))
      .pipe(filter(values => !values.isSilenced))
      .pipe(takeUntil(this.close$))
      .subscribe(_ => {
        if (this.flatNodes.every(node => node.isChecked)) {
          this.checkedChangeSubject$.next(TreeCheckNodeModelValue.checked());
        } else if (this.flatNodes.some(node => node.isChecked || node.isChecked === undefined)) {
          this.checkedChangeSubject$.next(TreeCheckNodeModelValue.indeterminate());
        } else {
          this.checkedChangeSubject$.next(TreeCheckNodeModelValue.unChecked());
        }
      });
  }

  static newBuilder(): TreeBuilder {
    return TreeBuilder.newBuilder();
  }

  close(): void {
    this.close$.complete();
    this.flatNodes.forEach(node => node.close());
  }

  protected setValueSilenced(value: boolean | undefined): void {
    this.checkedChangeSubject$.next(TreeCheckNodeModelValue.silenced(value));
    this.flatNodes.forEach(node => node.setValueSilenced(value));
  }
}

class TreeCheckNodeModelValue {
  private constructor(
    readonly isChecked: boolean | undefined,
    readonly isSilenced: boolean,
  ) {
  }

  static silenced(value: boolean | undefined): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(value, true);
  }

  static checked(): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(true, false);
  }

  static unChecked(): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(false, false);
  }

  static indeterminate(): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(undefined, false);
  }

  static of(value: boolean | undefined): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(value, false);
  }
}
