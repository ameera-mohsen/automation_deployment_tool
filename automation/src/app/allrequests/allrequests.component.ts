import { Component, OnInit } from '@angular/core';
import { User } from '../_models';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { UserService, SearchService } from '../_services';
import { Request } from '../_models';
import { Router } from '@angular/router';
import { DialogComponent } from '../dialog/dialog.component';
import { RequestInfo} from '../_models';

@Component({
  selector: 'app-allrequests',
  templateUrl: './allrequests.component.html',
  styleUrls: ['../css/Admin.min.css', '../css/bootstrap.min.css', '../css/_all-skins.min.css']
})
export class AllrequestsComponent implements OnInit {

  id: string;
    displayName: string;
    email: string;
    group: string;

  public searchString: string;
  public defectsearchString: string;

  allRequestBody: ResponseBody[];
  resBody = {} as ResponseBody;
  resStatus = {} as ResponseStatus;
  requestInfo: RequestInfo[];
  reqInfo = {} as RequestInfo;

  constructor(private router: Router, private userService: UserService, private searchService: SearchService) {
  }


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


    this.loadAllDeployments();
  }


  reqDetails(req: ResponseBody): void {
    window.localStorage.removeItem("viewReqId");
    window.localStorage.setItem("viewReqId", req.id.toString());
    // TODO
    // will create component to view all request Data
    // this.router.navigate(['requestDetails']);
  }

  loadAllDeployments() {
    console.log('Load all deployments.......');
    let res = this.searchService.getAllRequests().subscribe((data: Request) => {
      this.allRequestBody = data.responseBody;
      // console.log(' <<<<<< loadAllDeployments.id  >>>>>> ' + this.resBody.id );
    });
  }

  cancelRequest2(req_id:number,req_status:string): void{
    
    window.localStorage.removeItem("viewReqId");
    window.localStorage.setItem("viewReqId", req_id.toString());
    if (req_status =='PENDING_CANCEL') {
        req_status='CANCELED';
    }else {
        req_status='PENDING_CANCEL';  
    }
    
    console.log("req ID "+ req_id);
   // this.buildRequest(req);
    this.searchService.updateRequest(req_id.toString(),req_status)
    .subscribe(
      (data: Request) => {
          console.log(data);
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
setCancelReqParamters(req:ResponseBody):void{
  window.localStorage.removeItem("viewReqId");
  window.localStorage.setItem("viewReqId", req.id.toString());
  window.localStorage.removeItem("ReqStatus");
  window.localStorage.setItem("ReqStatus", req.status);
}


cancelRequest(): void{
    
 var req_status =   window.localStorage.getItem("ReqStatus");
 var req_id = window.localStorage.getItem("viewReqId");
  if (req_status =='PENDING_CANCEL') {
      req_status='CANCELED';
  }else {
      req_status='PENDING_CANCEL';  
  }
  
  console.log("req ID "+ req_id  + " req_status "+req_status);
 // this.buildRequest(req);
  this.searchService.updateRequest(req_id.toString(),req_status)
  .subscribe(
    (data: Request) => {
        console.log(data);
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
buildRequest(req: ResponseBody) {
    // fill resBody with data from Form
    this.resBody.status = req.status;
    this.reqInfo.comment = 'cancel request';
    this.requestInfo = [this.reqInfo];
    this.resBody.requestInfo = this.requestInfo;
    console.log(this.resBody);
  }

  show(req: ResponseBody){
    this.group = window.localStorage.getItem("group");
    console.log (this.group);
    if((req.status !='COMPLETED' && req.status!='CANCELED' && req.status!='IN_PROGRESS' && req.status !='PENDING_CANCEL' && this.group == 'DEVELOPMENT') ||(req.status =='PENDING_CANCEL' && this.group == 'Testing') ) {
      return true;
    }
    return false;
  }
}
