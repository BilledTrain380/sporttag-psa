import {RouterModule, Routes} from '@angular/router';
import {NgModule} from '@angular/core';
import {UserPageComponent} from './users/user-page.component';

const routes: Routes = [{
    path: 'users',
    component: UserPageComponent,
}];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule],
})
export class SettingsRoutingModule {}
