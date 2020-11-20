import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from "@angular/router";
import { Store } from "@ngrx/store";
import { Observable } from "rxjs";
import { map, tap } from "rxjs/operators";

import { selectAuthorities } from "../store/user/user.selector";

@Injectable({
              providedIn: "root",
            })
export class AuthorityAuthGuard implements CanActivate, CanActivateChild {
  constructor(
    private readonly store: Store,
    private readonly router: Router,
  ) {
  }

  // tslint:disable-next-line:max-line-length
  canActivate(route: ActivatedRouteSnapshot, _: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const requiredAuthorities: ReadonlyArray<string> | undefined = route.data.requiredAuthorities;

    return this.evaluateAuthorities(requiredAuthorities);
  }

  // tslint:disable-next-line:max-line-length
  canActivateChild(childRoute: ActivatedRouteSnapshot, _: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const requiredAuthorities: ReadonlyArray<string> | undefined = childRoute.parent?.data?.requiredAuthorities;

    return this.evaluateAuthorities(requiredAuthorities);
  }

  private evaluateAuthorities(requiredAuthorities: ReadonlyArray<string> | undefined): Observable<boolean> | boolean {
    if (requiredAuthorities) {
      return this.store.select(selectAuthorities)
        .pipe(map(authorities => requiredAuthorities.some(authority => authorities.includes(authority))))
        .pipe(tap(canActivate => {
          if (!canActivate) {
            this.router.navigate(["/pages/404"]);
          }
        }));
    }

    return false;
  }
}
