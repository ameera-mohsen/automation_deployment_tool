import { Component, OnInit, Inject, Output, Input , EventEmitter} from '@angular/core';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { SearchService } from '../_services';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { Request, RequestInfo, User } from '../_models';
import {  AuthenticationService} from '../_services';
import {formatDate } from '@angular/common';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html'
})
export class DetailsComponent implements OnInit {
  allRequestBody: ResponseBody[];
  resBody = {} as ResponseBody;
  resStatus = {} as ResponseStatus;
  detailForm: FormGroup;
  requestInfoArr: Array<RequestInfo>;
  reqInfo = {} as RequestInfo;
  status: string[] = ['APPROVED', 'REJECTED','PENDING_APPROVAL','PENDING_VERIFICATION','IN_PROGRESS','INFO_REQUESTED','INFO_SUBMITTED','COMPLETED','CANCELED','POSTPONED'];
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
  constructor(
    private formBuilder: FormBuilder,
    private authenticationService: AuthenticationService,
    private router: Router,
    public searchService: SearchService) { }

  ngOnInit() {
    let userData = window.localStorage.getItem("user");
      if(!userData) {
          this.router.navigate(['login']);
          return;
      }
      this.id = window.localStorage.getItem("id");
      this.displayName = window.localStorage.getItem("displayName");
      this.email = window.localStorage.getItem("email");
      // this.isRequestSubjectDisabled = false;
        let reqId = window.localStorage.getItem("viewReqId");
        if(!reqId) {
            alert("Invalid action.")
            this.router.navigate(['home']);
            return;
          }

        this.detailForm = this.formBuilder.group({
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
            status: [this.status],
            displayName:['']
          });
          
          this.searchService.searchRequestById(reqId)
          .subscribe( data => {
            this.detailForm.setValue({
                resBody : data,
                id: data.responseBody.id,
                currentStatus: data.responseBody.status,
                deploymentTime: data.responseBody.deploymentTime,
                environemnt:data.responseBody.environment,
                layer:data.responseBody.layer,
                defectId:data.responseBody.defectId,
                assignOnGroup:data.responseBody.assignOnGroup,
                requestDate:data.responseBody.requestDate,
                reason:data.responseBody.reason,
                releaseNote:data.responseBody.releaseNote,
                affectedService:data.responseBody.affectedService,
                deploymentComment: '',              
                requestSubject:data.responseBody.requestSubject,               
                status: this.selectedStatus,
                displayName: data.responseBody.initiatorUser.displayName,       
            });
            console.log(data.responseBody.requestSubject);
            this.requestInfoArr = data.responseBody.requestInfo; 
            this.resBody= data.responseBody;
          }); 
  }
  BackToList(): void{
    this.router.navigate(['listAll']);

}
  logout() {
    this.authenticationService.logout();   
}

}
