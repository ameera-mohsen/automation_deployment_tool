import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';

import { AuthGuard } from './_guards';
import { AllrequestsComponent } from './allrequests/allrequests.component';
import { EditRequestComponent } from './edit/edit.component';
import { NewDeploymentComponent } from './new/new.component';
import { AlertComponent } from './_directives';
import { ProgressSpinnerOverviewExample } from './common/progress-spinner-overview-example';

const appRoutes: Routes = [
    { path: 'home', component: HomeComponent},// , canActivate: [AuthGuard] 
    { path: 'login', component: LoginComponent },
    { path: 'edit', component: EditRequestComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'listAll', component: AllrequestsComponent},
    { path: 'new', component: NewDeploymentComponent},    
    { path: 'progress-spinner-overview-example', component: ProgressSpinnerOverviewExample},    

    
    // otherwise redirect to home
    { path: '**', redirectTo: 'home' }
];

export const routing = RouterModule.forRoot(appRoutes);