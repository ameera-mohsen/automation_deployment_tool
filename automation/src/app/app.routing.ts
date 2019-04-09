import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';
import { AuthGuard } from './_guards';
import { NewDeploymentComponent } from './new-deployment/new-deployment.component';

const appRoutes: Routes = [
 //   { path: '', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'newRequest', component: NewDeploymentComponent}
    // otherwise redirect to home
  //  { path: '**', redirectTo: '' }
]; 

export const routing = RouterModule.forRoot(appRoutes);