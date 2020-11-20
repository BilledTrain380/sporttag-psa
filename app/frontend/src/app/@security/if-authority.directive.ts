import { Directive, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef } from "@angular/core";
import { Store } from "@ngrx/store";
import { Subject } from "rxjs";
import { takeUntil } from "rxjs/operators";

import { selectAuthorities } from "../store/user/user.selector";

@Directive({
             selector: "[appIfAuthority]",
           })
export class IfAuthorityDirective implements OnInit, OnDestroy {

  @Input()
  set appIfAuthority(authorities: ReadonlyArray<string>) {
    this.wantedAuthorities = authorities;
    this.evaluateAuthorities();
  }

  private wantedAuthorities: ReadonlyArray<string> = [];

  private authorities: ReadonlyArray<string> = [];

  private readonly destroy$ = new Subject();

  constructor(
    // tslint:disable-next-line:no-any
    private readonly templateRef: TemplateRef<any>,
    private readonly viewContainer: ViewContainerRef,
    private readonly store: Store,
  ) {
  }

  ngOnInit(): void {
    this.store.select(selectAuthorities)
      .pipe(takeUntil(this.destroy$))
      .subscribe(authorities => {
        this.authorities = authorities;
        this.evaluateAuthorities();
      });
  }

  ngOnDestroy(): void {
    this.destroy$.complete();
  }

  private evaluateAuthorities(): void {
    if (this.wantedAuthorities.some(authority => this.authorities.includes(authority))) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}
