import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {InMemoryPreviousRouteService, PREVIOUS_ROUTE_SERVICE} from './prevous-route.service';

@NgModule({
    imports: [
        CommonModule,
    ],
    declarations: [],
})
export class PreviousRouteModule {
    static forRoot(): ModuleWithProviders {
        return {
            ngModule: PreviousRouteModule,
            providers: [
                {
                    provide: PREVIOUS_ROUTE_SERVICE,
                    useClass: InMemoryPreviousRouteService,
                },
            ],
        };
    }
}
