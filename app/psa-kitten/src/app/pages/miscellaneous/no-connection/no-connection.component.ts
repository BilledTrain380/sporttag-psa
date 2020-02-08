import {Component, Inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {PREVIOUS_ROUTE_SERVICE, PreviousRouteService} from '../../../modules/previous-route/prevous-route.service';

@Component({
    selector: 'ngx-no-connection',
    templateUrl: './no-connection.component.html',
    styleUrls: ['./no-connection.component.scss'],
})
export class NoConnectionComponent implements OnInit {

    constructor(
        private readonly router: Router,

        @Inject(PREVIOUS_ROUTE_SERVICE)
        private readonly previousRouteService: PreviousRouteService,
    ) {}

    ngOnInit() {
    }

    retry(): void {
        this.router.navigate([this.previousRouteService.previousUrl]);
    }
}
