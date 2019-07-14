import { Component, OnInit, Inject, Output, Input , EventEmitter} from '@angular/core';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { SearchService } from '../_services';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { Request, RequestInfo, User } from '../_models';
import {StatusService ,  AuthenticationService} from '../_services';
import { observable, Observable } from 'rxjs';
import { first, isEmpty } from 'rxjs/operators';

@Component({
    selector: 'editrequests',
    templateUrl: './edit.component.html',
    styleUrls: ['../css/Admin.min.css', '../css/bootstrap.min.css', '../css/_all-skins.min.css']
})

export class EditRequestComponent implements OnInit {

    //newStatus: String;
    //deploymentReqId: String;

    allRequestBody: ResponseBody[];
    resBody = {} as ResponseBody;
    resStatus = {} as ResponseStatus;
    editForm: FormGroup;
    requestInfo: RequestInfo[];
    reqInfo = {} as RequestInfo;
    //status: string[] = ['APPROVED', 'REJECTED','PENDING_APPROVAL','PENDING_VERIFICATION','IN_PROGRESS','INFO_REQUESTED','INFO_SUBMITTED','COMPLETED','CANCELED','POSTPONED'];
    status: string[]=[];
    selectedStatus: string = '';
    id: string;
    environemnt: string;
    layer: string;
    defectId:string;
    assignOnGroup:string;
    requestDate:string;
    reason:string;
    releaseNote:string;
    affectedService:string;
    displayName: string;
    email: string;
    requestSubject: string;

    assignOnUser = {} as User;
    pickedByUser = {} as User;
    initiatorUser = {} as User;

    isRequestSubjectDisabled:boolean;

    
    constructor(private formBuilder: FormBuilder,private authenticationService: AuthenticationService,private router: Router,public searchService: SearchService) { }

  


    ngOnInit() {
      let userData = window.localStorage.getItem("user");
      if(!userData) {
          //console.log("Loggedin User :  "  + userData);
         // alert("Invalid action. User is Not loggedIn")
          this.router.navigate(['login']);
          return;
      }
      this.id = window.localStorage.getItem("id");
      this.displayName = window.localStorage.getItem("displayName");
      this.email = window.localStorage.getItem("email");
      // this.isRequestSubjectDisabled = false;
        let reqId = window.localStorage.getItem("editReqId");
        if(!reqId) {
            alert("Invalid action.")
            this.router.navigate(['home']);
            return;
          }

        this.editForm = this.formBuilder.group({
          resBody: [],
            id: [''],
            environemnt:[''],
            layer:[''],
            currentStatus: ['', Validators.required],
            defectId:[''],
            assignOnGroup:[''],
            requestDate:[''],
            reason:[''],
            releaseNote:[''],
            affectedService:[''],
            deploymentTime: ['', Validators.required],
            deploymentComment: [''],
            requestSubject: [''],
            status: [this.status]
          });
          
          this.searchService.searchRequestById(reqId)
          .subscribe( data => {
            this.editForm.setValue({
                resBody : data,
                id: data.responseBody.id,
                currentStatus: data.responseBody.status,
                deploymentTime: data.responseBody.deploymentTime,
                //myedit ******
                environemnt:data.responseBody.environment,
                layer:data.responseBody.layer,
                defectId:data.responseBody.defectId,
                assignOnGroup:data.responseBody.assignOnGroup,
                requestDate:data.responseBody.requestDate,
                reason:data.responseBody.reason,
                releaseNote:data.responseBody.releaseNote,
                affectedService:data.responseBody.affectedService,
                deploymentComment: '',
                // deploymentComment: data.responseBody.requestInfo.comment,              
                requestSubject:data.responseBody.requestSubject,               
                status: this.selectedStatus,           
            });
            if (data.responseBody.assignOnGroup=="DEPLOYMENT") {
              this.isRequestSubjectDisabled=false;
            } else {
              this.isRequestSubjectDisabled=true;
            }
            console.log("Current status is " + data.responseBody.status);
            console.log("Assigned Group is " + data.responseBody.assignOnGroup);
            this.searchService.getAllowedStatusesList(data.responseBody.status, data.responseBody.assignOnGroup).pipe(first()).subscribe(statuses => {
                  this.status = statuses;
            });            
            //this.status= ['APPROVED', 'REJECTED','PENDING_APPROVAL','PENDING_VERIFICATION','IN_PROGRESS','INFO_REQUESTED','INFO_SUBMITTED','COMPLETED','CANCELED','POSTPONED'], 
            this.resBody= data.responseBody;          
          }); 
    }

    buildRequest() {
      // fill resBody with data from Form
      this.resBody.status = this.editForm.get('status').value;
      this.reqInfo.comment = this.editForm.get('deploymentComment').value;
      this.resBody.requestSubject = this.editForm.get('requestSubject').value;
      this.requestInfo = [this.reqInfo];
      this.resBody.requestInfo = this.requestInfo;
    }

    onSubmit() {
       // console.log("OnUpdate :: ReqId:  "  + this.editForm.get('id').value + " and statu:>>>  " +  this.editForm.get('status').value)
        // this.searchService.updateRequest(this.editForm.get('id').value,  this.editForm.get('status').value)
        //   .subscribe(
        //     (data: Request) => {
        //       if(data.responseStatus.statusCode === 200) {
        //         alert('Request updated successfully.');
        //         this.router.navigate(['home']);
        //       }else {
        //         alert(data.responseStatus+ " will take you Home ");
        //         this.router.navigate(['home']);
        //       }
        //     },
        //     error => {
        //       alert(error);
        //     });

        this.buildRequest();
        //console.log('blablablaaaaaaaa' + this.resBody.reason);
        //this.searchService.updateRequestObj(this.resBody)
        console.log(this.resBody.id);
        console.log(this.resBody.status);
        console.log("Subject is -- " + this.resBody.requestSubject);
        console.log("comment is -- " + this.reqInfo.comment);
        this.searchService.updateRequestData(this.resBody.id,this.resBody.status, this.resBody.requestSubject)
        // this.searchService.updateRequest(this.resBody.id,this.resBody.status)
          .subscribe(
            (data: Request) => {
              if(data.responseStatus.statusCode === 200) {
                alert('Request updated successfully.');
                this.router.navigate(['home']);
              }else {
                alert(data.responseStatus+ " will take you Home ");
                this.router.navigate(['home']);
              }
            },
            error => {
              alert(error);
            });
      }
      logout() {
        // remove user from local storage to log user out
        // this.router.navigate(['login']);
        // localStorage.removeItem('user');
        this.authenticationService.logout();
    
       
    }
      

}
