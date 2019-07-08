import { Component, OnInit } from '@angular/core';
import { User } from '../_models';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { UserService, SearchService } from '../_services';
import { Request } from '../_models';
import { Router } from '@angular/router';
import { DialogComponent } from '../dialog/dialog.component';
import { RequestInfo} from '../_models';
import {  AuthenticationService} from '../_services';

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

  constructor(private router: Router, private authenticationService: AuthenticationService, private userService: UserService, private searchService: SearchService) {
  }


  ngOnInit() {
    let userData = window.localStorage.getItem("user");
      if(!userData) {
          console.log("Loggedin User :  "  + userData);
          //alert("Invalid action. User is Not loggedIn")
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

  cancelRequest(req: ResponseBody): void{
    if (req.status =='PENDING_CANCEL') {
        req.status='CANCELED';
    }else {
        req.status='PENDING_CANCEL';  
    }
    
    console.log(req.id);
    this.searchService.updateRequest(req.id.toString(),req.status)
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


  show(req: ResponseBody){
    this.group = window.localStorage.getItem("group");
    console.log (this.group);
    if((req.status !='COMPLETED' && req.status!='CANCELED' && req.status!='IN_PROGRESS' && req.status !='PENDING_CANCEL' && this.group == 'DEVELOPMENT') ||(req.status =='PENDING_CANCEL' && this.group == 'Testing') ) {
      return true;
    }
    return false;
  }
  logout() {
    // remove user from local storage to log user out
    //this.router.navigate(['login']);
    this.authenticationService.logout();
    
   
}
displayname( displayname :string){
  var array = displayname.split(" ");
  for (var  i=0;i< array.length ;i++ ){
      array[i]=array[i].charAt(0).toUpperCase() +array[i].slice(1)
  }
  var display="";
  for (var  i=0;i< array.length ;i++ ){
      display+=array[i]+" ";
  }
  return display;
  }
}
