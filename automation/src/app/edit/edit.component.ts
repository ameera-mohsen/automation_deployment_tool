import { Component, OnInit, Inject, Output, Input , EventEmitter} from '@angular/core';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { SearchService } from '../_services';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import { Request, RequestInfo } from '../_models';

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
    status: string[] = ['APPROVED', 'REJECTED','PENDING','IN_PROGRESS','INFO_REQUESTED','INFO_SUBMITTED','COMPLETED','CANCELED,POST_PONED'];
    selectedStatus: string = '';
    id: string;
    displayName: string;
    email: string;

    
    constructor(private formBuilder: FormBuilder,private router: Router,public searchService: SearchService) { }

  


    ngOnInit() {
      let userData = window.localStorage.getItem("user");
      if(!userData) {
          console.log("Loggedin User :  "  + userData);
          alert("Invalid action. User is Not loggedIn")
          this.router.navigate(['login']);
          return;
      }
      this.id = window.localStorage.getItem("id");
      this.displayName = window.localStorage.getItem("displayName");
      this.email = window.localStorage.getItem("email");


      
        let reqId = window.localStorage.getItem("editReqId");
        if(!reqId) {
            alert("Invalid action.")
            this.router.navigate(['home']);
            return;
          }

        this.editForm = this.formBuilder.group({
            id: [''],
            currentStatus: ['', Validators.required],
            deploymentTime: ['', Validators.required],
            deploymentComment: [''],
            status: [this.status]
          });
          
          this.searchService.searchRequestById(reqId)
          .subscribe( data => {
            this.editForm.setValue({
                id: data.responseBody.id,
                currentStatus: data.responseBody.status,
                deploymentTime: data.responseBody.deploymentTime,
                deploymentComment: data.responseBody.requestInfo,
                status: this.selectedStatus
            });
          });
    }

    onSubmit() {
       // console.log("OnUpdate :: ReqId:  "  + this.editForm.get('id').value + " and statu:>>>  " +  this.editForm.get('status').value)
        this.searchService.updateRequest(this.editForm.get('id').value,  this.editForm.get('status').value)
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

}
