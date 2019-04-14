import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';

import { AlertService, AuthenticationService, UserService } from '../_services';
import { UserCredentials } from '../_models/userCredentials';
import { ResponseStatus } from '../_models/responseStatus';
import { ResponseBody } from '../_models/responseBody';
import { UserCredentialsResBody } from '../_models/userCredentialsResBody';

@Component({ templateUrl: 'login.component.html' })
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl: string;
    resBody = {} as ResponseBody;
    resStatus = {} as ResponseStatus;
    userCred = {} as UserCredentials;
    userResBody: UserCredentialsResBody;

    constructor(
        private formBuilder: FormBuilder,
        private userServ: UserService,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService,
        private alertService: AlertService) { }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', Validators.required]
        });

        // reset login status
        this.authenticationService.logout();
        // get return url from route parameters or default to '/'
        // this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }

    // convenience getter for easy access to form fields
    get f() { return this.loginForm.controls; }

    onSubmit() {
        console.log(" entering onSubmit ");
        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }

        this.userCred.email = this.f.email.value;
        this.userCred.userPassword = this.f.password.value;
        this.loading = true;
        this.userServ.login(this.userCred)
            .subscribe(
                data => {
                    this.userResBody = data
                    let id = this.userResBody.responseBody._id;
                    let displayName = this.userResBody.responseBody.displayName;
                    let email = this.userResBody.responseBody.email;
                    let group = this.userResBody.responseBody.groups.groupNames;
                    window.localStorage.removeItem("user");
                    window.localStorage.removeItem("id");
                    window.localStorage.removeItem("displayName");
                    window.localStorage.removeItem("email");
                    window.localStorage.removeItem("group");

                    window.localStorage.setItem("user", data);
                    window.localStorage.setItem("id", id);
                    window.localStorage.setItem("displayName", displayName);
                    window.localStorage.setItem("email", email);
                    window.localStorage.setItem("group", group);

                    this.router.navigate(['home']);
                },
                error => {
                    this.alertService.error(error);
                    this.loading = false;
                });
    }
}
