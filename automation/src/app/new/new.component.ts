import { Component, OnInit } from '@angular/core';
import { User, Layer, Status, Service, Environment } from '../_models';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { UserService, SearchService, StatusService, EnvironmentService, ServiceListService, LayersService } from '../_services';
import { Request } from '../_models';
import { Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { FormBuilder, FormGroup, FormArray, Validators, FormControl } from '@angular/forms';
import { formatDate } from '@angular/common';

@Component({
  selector: 'new',
  templateUrl: './new.component.html',
  styleUrls: ['../css/Admin.min.css', '../css/bootstrap.min.css', '../css/_all-skins.min.css']
})
export class NewDeploymentComponent implements OnInit {

  

  assignOnUser: User = {
    "userId": "5c49c60e1b8f732ffcff44de",
    "displayName": "Sara Omran",
    "email": "sara.omran@dxc.com",
    "groups": "TESTING"
  };

  pickedByUser: User = {
    "userId": "5c49c60e1b8f732ffcff44de",
    "displayName": "Sara Omran",
    "email": "sara.omran@dxc.com",
    "groups": "TESTING"
  };

  initiatorUser = {} as User;

  allRequestBody: ResponseBody[];
  resBody = {} as ResponseBody;
  resStatus = {} as ResponseStatus;
  status: object[];
  users: User[] = [];
  selectedLayer = [''];


  IIB: boolean = false;
  WPS: boolean = false;
  WSRR: boolean = false;
  WAS: boolean = false;
  DB: boolean = false;
  DP: boolean = false;
  ODM: boolean = false;
  marked = false;
  theCheckbox = false;

  environment: string[] = ['SIT', 'PT', 'DEV', 'MFT'];
  layers = ['IIB', 'WPS', 'WSRR', 'WAS', 'DP', 'DB', 'WSRR', 'WAS'];
  affectedService = ['Payment', 'Refund', 'Upload', 'Common', 'Customer', 'Account', 'Cleanup'];
  selectedAffectedService: string = '';
  selectedenv: string = '';
  Reason: String = '';
  today = new Date();
  jstoday = '';
  newForm: FormGroup;

  defectId = '';
  releaseNote = '';
  reason = '';
  id: string;
  displayName: string;
  email: string;
  group: string;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService, private layerService: LayersService,
    private statusService: StatusService, private serviceListService: ServiceListService,
    private environmentService: EnvironmentService, private searchService: SearchService) {
  }

  ngOnInit() {
    let userData = window.localStorage.getItem("user");
    this.group = window.localStorage.getItem("group");

    if (!userData) {
      console.log("Loggedin User :  " + userData);
      alert("Invalid action. User is Not loggedIn")
      this.router.navigate(['login']);
      return;
    }

    if (this.group !== 'DEVELOPMENT') {
      console.log(" Only Development Team Can initiate new Deployment Request");
      this.router.navigate(['home']);
      return;
    }
    


    this.id = window.localStorage.getItem("id");
    this.displayName = window.localStorage.getItem("displayName");
    this.email = window.localStorage.getItem("email");

    this.jstoday = formatDate(this.today, 'yyyy-MM-ddTHH:mm:ss', 'en-US', '+0530');
    this.newForm = this.formBuilder.group({
      Environment: [this.environment.values, Validators.required],
      Layers: [this.selectedLayer, Validators.required],
      status: ['IN_PROGRESS'],
      defectId: [this.defectId, Validators.required],
      assignOnGroup: ['TESTING'],
      requestDate: [this.jstoday, Validators.required],
      deploymentTime: [this.jstoday, Validators.required],
      reason: [this.reason, Validators.required],
      releaseNote: [this.releaseNote, Validators.required],
      affectedService: [this.selectedAffectedService, Validators.required],
    });

  }


  buildRequestJson() {
    // fill resBody with data from Form
    //console.log(this.newForm.value);
    this.getCheckBox();
    this.selectedenv = this.newForm.get('Environment').value;
    this.layers = this.newForm.get('Layers').value;
    this.selectedAffectedService = this.newForm.get('affectedService').value;
    this.resBody.environment = this.selectedenv;
    this.resBody.layer = this.selectedLayer;
    //this.resBody.affectedService = this.selectedAffectedService;
    this.resBody.status = this.newForm.get('status').value;
    this.resBody.defectId = this.newForm.get('defectId').value;
    this.resBody.assignOnGroup = this.newForm.get('assignOnGroup').value;
    this.resBody.requestDate = this.newForm.get('requestDate').value;
    this.resBody.deploymentTime = this.newForm.get('deploymentTime').value;
    this.resBody.reason = this.newForm.get('reason').value;
    this.resBody.releaseNote = this.newForm.get('releaseNote').value;
    this.initiatorUser.userId= this.id;
    this.initiatorUser.displayName = this.displayName;
    this.initiatorUser.email= this.email;
    this.initiatorUser.groups = 'DEVELOPMENT';
    this.resBody.initiatorUser = this.initiatorUser;
    
    this.resBody.assignOnUser = this.assignOnUser;
    this.resBody.pickedByUser = this.pickedByUser;
  }

  onSubmit() {
    this.buildRequestJson();
    this.searchService.newRequest(this.resBody).
      subscribe(
        (data: Request) => {
          if (data.responseStatus.statusCode === 200) {
            alert('Request updated successfully.');
            this.router.navigate(['home']);
          } else {
            alert(data.responseStatus + " will take you Home ");
            this.router.navigate(['home']);
          }
        },
        error => {
          alert(" Date msh Valid :: " + error);
          this.router.navigate(['home']);
        });
  }




  getCheckBox() {
    if (this.WPS === true) {
      this.selectedLayer.push("WPS");
      console.log(" WPS True: ");

    }
    if (this.IIB === true) {
      this.selectedLayer.push("IIB");
      console.log(" IIB True: ");

    }
    if (this.WSRR === true) {
      this.selectedLayer.push("WSRR");
      console.log(" WSRR True: ");

    }
    if (this.WAS === true) {
      this.selectedLayer.push("WAS");
      console.log(" WAS True: ");

    }
    if (this.DP === true) {
      this.selectedLayer.push("DP");
      console.log(" DP True: ");

    }
    if (this.DB === true) {
      this.selectedLayer.push("DB");
      console.log(" DB True: ");


    }
    if (this.ODM === true) {
      console.log(" ODM True: ");

      this.selectedLayer.push("ODM");

    }
    console.log(" layers log: " + this.selectedLayer);

  }


  toggleVisibility(e: any) {
    this.marked = e.target.checked;

  }

}
