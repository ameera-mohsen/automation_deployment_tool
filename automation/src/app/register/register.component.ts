import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import {  AuthenticationService} from '../_services';
import { AlertService, UserService } from '../_services';
import { UserBody } from '../_models/UserBody';
import { Groups } from '../_models/groups';
import { Privilege } from '../_models/privilege';
import { UserCredentials } from '../_models/userCredentials';

@Component({templateUrl: 'register.component.html'})
export class RegisterComponent implements OnInit {
    registerForm: FormGroup;
    loading = false;
    submitted = false;
    userBody = {} as UserBody;
    groups: string[] = ['DEVELOPMENT', 'DEPLOYMENT','TESTING'];
    mail: string;
    username: string;
    number: string;
    password: string;
    userStatus : string ="ACTIVE";
     
    group = {} as Groups;
    privilege = {} as Privilege;
    credentials = {} as UserCredentials;
    constructor(
        private formBuilder: FormBuilder,
        private router: Router,
        private userService: UserService,
        private alertService: AlertService) { }

    ngOnInit() {
        this.registerForm = this.formBuilder.group({
            mail: [this.mail, Validators.required],
            username: [this.username, Validators.required],
            number: [this.number, Validators.required],
            password: [this.password, [Validators.required, Validators.minLength(6)]],
            Group: [this.groups.values, Validators.required],
        });
    }

    // convenience getter for easy access to form fields
    get f() { return this.registerForm.controls; }

    onSubmit() {
        this.submitted = true;

        // stop here if form is invalid
        if (this.registerForm.invalid) {
        
            return;
        }

        this.loading = true;
        console.log("in registeration method");
        console.log(this.registerForm.value);
        this.buildRequest();
        this.userService.register(this.userBody)
            .pipe(first())
            .subscribe(
                data => {
                    alert('Registration successful.');
                   // this.alertService.success('Registration successful', true);
                    this.router.navigate(['/login']);
                },
                error => {
                    this.alertService.error(error);
                    this.loading = false;
                });
    }
    buildRequest(){
       console.log(this.f.username.value);
       console.log(this.f.password.value);
       console.log(this.f.number.value);
       console.log(this.f.mail.value);
       this.userBody.displayName=this.f.username.value;
       this.userBody.email=this.f.mail.value;
       this.userBody.userStatus=this.userStatus;
       this.userBody.number=this.f.number.value;
       this.group.groupNames = this.f.Group.value;
       this.userBody.groups=this.group;
       this.privilege.level=1;
       this.userBody.privilege=this.privilege;
       this.credentials.email=this.f.mail.value;
       this.credentials.userName=this.f.username.value;
       this.credentials.userPassword=this.f.password.value;
       this.userBody.credentials=this.credentials;


    }

  
  
}
