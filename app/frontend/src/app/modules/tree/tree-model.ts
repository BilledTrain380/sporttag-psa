import { BehaviorSubject, merge, Observable, Subject } from "rxjs";
import { filter, map, takeUntil } from "rxjs/operators";

import { MAX_COLUMN_SIZE } from "../../@theme/theme-constants";
import { buildColumnCssClass } from "../../@theme/utils/css-classes-utils";

export class TreeBuilder {
  private label = "";
  private splitter = 1;
  private readonly nodes: Array<TreeBuilder> = [];

  private constructor() {
  }

  static newBuilder(): TreeBuilder {
    return new TreeBuilder();
  }

  setLabel(label: string): TreeBuilder {
    this.label = label;

    return this;
  }

  setSplitter(splitter: number): TreeBuilder {
    this.splitter = splitter;

    return this;
  }

  addLeafNode(label: string): TreeBuilder {
    const node = TreeBuilder.newBuilder()
      .setLabel(label);
    this.nodes.push(node);

    return this;
  }

  addNode(node: TreeBuilder): TreeBuilder {
    this.nodes.push(node);

    return this;
  }

  build(): TreeCheckNodeModel {
    const chunkSize = Math.max(Math.floor(this.nodes.length / this.splitter), 1);
    const nodeChildren = this.nodes.map(node => node.build());
    const nodeChunks: Array<ReadonlyArray<TreeCheckNodeModel>> = [];
    for (let i = 0; i < nodeChildren.length; i += chunkSize) {
      const chunk = nodeChildren.slice(i, i + chunkSize);
      nodeChunks.push(chunk);
    }

    return new TreeCheckNodeModel(this.label, this.splitter, nodeChunks);
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

  readonly flatNodes: ReadonlyArray<TreeCheckNodeModel>;

  readonly isChecked$: Observable<boolean | undefined>;
  private readonly valueChanged$: Observable<TreeCheckNodeModelValue>;

  private readonly checkedChangeSubject$ = new BehaviorSubject(TreeCheckNodeModelValue.unChecked());
  private readonly close$ = new Subject();

  constructor(
    readonly label: string,
    readonly splitter: number = 1,
    readonly nodes: ReadonlyArray<ReadonlyArray<TreeCheckNodeModel>> = [],
  ) {
    const columnSize = Math.floor(MAX_COLUMN_SIZE / splitter);
    this.columnCssClass = buildColumnCssClass(columnSize);
    this.flatNodes = nodes.reduce((accumulator, value) => accumulator.concat(value), []);
    this.isChecked$ = this.checkedChangeSubject$.asObservable()
      .pipe(map(value => value.isChecked));
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
