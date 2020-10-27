import { BehaviorSubject, combineLatest, Observable, Subject } from "rxjs";
import { filter, map, takeUntil } from "rxjs/operators";

import { identityFunction, UnaryOperator } from "../../@core/lib/function";

export class TreeBuilder {
  private label = "";
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

  addLeafNode(label: string): TreeBuilder {
    const node = TreeBuilder.newBuilder()
      .setLabel(label);
    this.nodes.push(node);

    return this;
  }

  addNode(operator: UnaryOperator<TreeBuilder>): TreeBuilder {
    const builder = operator(TreeBuilder.newBuilder());
    this.nodes.push(builder);

    return this;
  }

  build(): TreeCheckNodeModel {
    const nodeModels = this.nodes
      .map(node => node.build());

    return new TreeCheckNodeModel(this.label, nodeModels);
  }
}

export class TreeCheckNodeModel {
  get isChecked(): boolean | undefined {
    return this.checkedChangeSubject$.value.isChecked;
  }

  set isChecked(value: boolean | undefined) {
    this.checkedChangeSubject$.next(TreeCheckNodeModelValue.of(value));
    this.nodes.forEach(node => node.setValueSilenced(value));
  }

  readonly isChecked$: Observable<boolean | undefined>;
  protected isCheckedUnsilenced$: Observable<boolean | undefined>;
  private readonly checkedChangeSubject$ = new BehaviorSubject(TreeCheckNodeModelValue.unChecked());
  private readonly close$ = new Subject();

  constructor(
    readonly label: string,
    readonly nodes: ReadonlyArray<TreeCheckNodeModel> = [],
  ) {
    this.isChecked$ = this.checkedChangeSubject$.asObservable()
      .pipe(map(value => value.isChecked));
    this.isCheckedUnsilenced$ = this.checkedChangeSubject$.asObservable()
      .pipe(filter(value => !value.isSilenced))
      .pipe(map(value => value.isChecked));

    combineLatest(nodes.map(node => node.isCheckedUnsilenced$))
      .pipe(takeUntil(this.close$))
      .subscribe(nodeCheckedList => {
        if (nodeCheckedList.every(identityFunction())) {
          this.checkedChangeSubject$.next(TreeCheckNodeModelValue.checked());
        } else if (nodeCheckedList.some(identityFunction())) {
          this.checkedChangeSubject$.next(TreeCheckNodeModelValue.intermediate());
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
    this.nodes.forEach(node => node.close());
  }

  protected setValueSilenced(value: boolean | undefined): void {
    this.checkedChangeSubject$.next(TreeCheckNodeModelValue.silenced(value));
    this.nodes.forEach(node => node.setValueSilenced(value));
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

  static intermediate(): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(undefined, false);
  }

  static of(value: boolean | undefined): TreeCheckNodeModelValue {
    return new TreeCheckNodeModelValue(value, false);
  }
}
