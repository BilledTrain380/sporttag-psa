import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

import {MiscellaneousComponent} from './miscellaneous.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {NoConnectionComponent} from './no-connection/no-connection.component';

const routes: Routes = [{
    path: '',
    component: MiscellaneousComponent,
    children: [{
        path: '404',
        component: NotFoundComponent,
    }, {
        path: 'no-connection',
        component: NoConnectionComponent,
    }],
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class MiscellaneousRoutingModule {
}

export const routedComponents = [
    MiscellaneousComponent,
    NotFoundComponent,
    NoConnectionComponent,
];
