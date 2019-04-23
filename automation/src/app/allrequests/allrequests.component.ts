import { Component, OnInit } from '@angular/core';
import { User } from '../_models';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { UserService, SearchService } from '../_services';
import { Request } from '../_models';
import { Router } from '@angular/router';

@Component({
  selector: 'app-allrequests',
  templateUrl: './allrequests.component.html',
  styleUrls: ['../css/Admin.min.css', '../css/bootstrap.min.css', '../css/_all-skins.min.css']
})
export class AllrequestsComponent implements OnInit {

  id: string;
    displayName: string;
    email: string;

  public searchString: string;
  public defectsearchString: string;

  allRequestBody: ResponseBody[];
  resBody = {} as ResponseBody;
  resStatus = {} as ResponseStatus;

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
}
