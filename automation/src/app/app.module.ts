import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { DialogComponent } from './dialog/dialog.component';
// used to create fake backend
import { fakeBackendProvider } from './_helpers';

import { AppComponent } from './app.component';
import { routing } from './app.routing';

import { AlertComponent } from './_directives';
import { AuthGuard } from './_guards';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { AlertService, AuthenticationService, UserService, LayersService, StatusService, ServiceListService, EnvironmentService, SearchService } from './_services';
import { HomeComponent } from './home';
import { LoginComponent } from './login';
import { RegisterComponent } from './register';;
import { AllrequestsComponent } from './allrequests/allrequests.component'
import { FilterPipe } from './pipes/filter';
import { EditRequestComponent } from './edit/edit.component';
import { NewDeploymentComponent } from './new/new.component';
import { ProgressSpinnerOverviewExample } from './common/progress-spinner-overview-example';
import { NgMultiSelectDropDownModule } from 'ng-multiselect-dropdown';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';




@NgModule({
    imports: [
        BrowserModule,
        ReactiveFormsModule,
        HttpClientModule,
        routing,
        FormsModule,
        BrowserAnimationsModule ,
        NgMultiSelectDropDownModule.forRoot()
    ],
    declarations: [
        AppComponent,
        DialogComponent,
        AlertComponent,
        HomeComponent,
        NewDeploymentComponent,
        ProgressSpinnerOverviewExample,
        LoginComponent,
        EditRequestComponent,
        FilterPipe,
        RegisterComponent,
        AllrequestsComponent
    ],
    providers: [
        AuthGuard,
        AlertService,
        AuthenticationService,
        UserService,
        LayersService,
        StatusService,
        ServiceListService,
        EnvironmentService,
        SearchService,
        { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
        { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },

        // provider used to create fake backend
        fakeBackendProvider
    ],
    exports: [
        FilterPipe
    ],
    bootstrap: [AppComponent]
})

export class AppModule { }