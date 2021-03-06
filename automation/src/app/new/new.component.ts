import { Component, OnInit } from '@angular/core';
import { User, Layer, Status, Service, Environment } from '../_models';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { UserService, SearchService, StatusService,AuthenticationService, EnvironmentService, ServiceListService, LayersService } from '../_services';
import { Request , RequestInfo } from '../_models';
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

					
							 
   selectedLayers = [
    { id: 100, name: 'WPS' },
    { id: 200, name: 'ODM' },
    { id: 300, name: 'IIB' },
    { id: 300, name: 'WAS' },
    { id: 400, name: 'DP' },
    { id: 400, name: 'DB' },
    { id: 400, name: 'WSRR' }
  ];
  					 
							 
	requestInfoArr : Array<RequestInfo> = [];						 
  reqInfo = {} as RequestInfo;

  assignOnUser: User = {
    "userId": "5c5807e7fb6fc0356792bd44",
    "displayName": "DEPLOYMENT TEAM",
    "email": "SADAD_AMS@dxc.com",
    "groups": "DEPLOYMENT"
  };

 
  pickedByUser: User = {
    "userId": "5c49c60e1b8f732ffcff44de",
    "displayName": "TESTING TEAM",
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
  MyselectedLayer=[];			 

layerString: string[] = [];
  IIB: boolean = false;
  WPS: boolean = false;
  WSRR: boolean = false;
  WAS: boolean = false;
  DB: boolean = false;
  DP: boolean = false;
  ODM: boolean = false;
  
  marked = false;
  theCheckbox = false;
   layers: Layer[] = [];
   affectedService: Service[] = [];
    environment: Environment[] = [];
  // environment: string[] = ['SIT', 'PT', 'MFT'];
  // layers = ['IIB', 'WPS', 'WSRR', 'WAS', 'DP', 'DB', 'ODM'];
  // affectedService = ['Payment', 'Refund', 'Upload', 'Common', 'Customer', 'Account', 'Cleanup'];
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

  dropdownList = [];
  selectedItems = [];
  selectedlayers=[];	
  dropdownSettings = {};
  dropdownSettingsServices ={};
  submitted=false;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService, private layerService: LayersService,
    private statusService: StatusService, private serviceListService: ServiceListService,
    private environmentService: EnvironmentService,private authenticationService: AuthenticationService, private searchService: SearchService) {
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

    //this.jstoday = formatDate(this.today, 'yyyy-MM-ddTHH:mm:ss', 'en-US', '+0530');
    this.jstoday = formatDate(this.today, 'yyyy-MM-ddTHH:mm:ss', 'en-EG');
    this.newForm = this.formBuilder.group({
   
	selectedLayers: new FormArray([]),								
      Environment: [this.environment.values, Validators.required],
      layers: [this.selectedLayer, Validators.required],
      status: ['NEW'],
      defectId: [this.defectId, Validators.required],
      assignOnGroup: ['DEPLOYMENT'],
      requestDate: [this.jstoday, Validators.required],
      deploymentTime: [this.jstoday, Validators.required],
      reason: [this.reason, Validators.required],
      releaseNote: [this.releaseNote, Validators.required],
    affectedService: ['', Validators.required],
    deploymentComment: ['']
    });

  // this.addCheckboxes();			 
  //  this.dropdownList = this.affectedService;
    // this.dropdownList = [
    //   'Payment' ,'Refund','Upload','Common' ,'Customer','Account','Cleanup'
    // ];
    /*this.selectedItems = [
      { item_id: 3, item_text: 'Pune' },
      { item_id: 4, item_text: 'Navsari' }
    ];*/
    

    this.dropdownSettings = {
      singleSelection: false,
      idField: 'id',
      textField: 'layerName',
      itemsShowLimit: 4,
      allowSearchFilter: true,
      enableCheckAll: true
    };
    this.dropdownSettingsServices = {
      singleSelection: false,
      idField: 'id',
      textField: 'serviceName',
      itemsShowLimit: 4,
      allowSearchFilter: true,
      enableCheckAll: true
    };

    this.loadLayers();
    this.loadServices();
    this.loadEnvironments();

    


  }

  // private addCheckboxes() {
  //   this.selectedLayers.map((o, i) => {
  //     const control = new FormControl(i === 0); // if first item set to true, else false
  //     (this.newForm.controls.selectedLayers as FormArray).push(control);
  //   });
  // }					   
	private ConvertLayerArrJsonToString():string[] {
   var listObj = this.newForm.get('layers').value;
  var  listString:string[] = [];
    for (var i = 0; i < listObj.length; i++) {
      listString[i] = listObj[i].layerName;
    }
    return listString;
  }																					
  private ConvertServiceArrJsonToString():string[] {
    var listObj = this.newForm.get('affectedService').value;
   var  listString:string[] = [];
     for (var i = 0; i < listObj.length; i++) {
       listString[i] = listObj[i].serviceName;
     }
     return listString;
   }																			
  private loadLayers() {
    this.layerService.getAll().pipe(first()).subscribe(layers => {
        this.layers = layers;
    });
}

private loadServices() {
    this.serviceListService.getAll().pipe(first()).subscribe(services => {
        console.log('search --------------------------');
        this.affectedService = services;
    });
}
private loadEnvironments() {
    this.environmentService.getAll().pipe(first()).subscribe(environments => {
        this.environment = environments;
        //console.log(this.environments);
    });

}

addRequestInfo(userId, displayName, comment){
  this.reqInfo.userId = userId;
  this.reqInfo.displayName = displayName;
  let today = new Date();
  this.reqInfo.time = formatDate(today, 'yyyy-MM-ddTHH:mm:ss', 'en-EG');
  this.reqInfo.comment = comment;
  console.log(this.reqInfo);
  this.requestInfoArr.push(this.reqInfo);
  console.log("array : "+this.requestInfoArr);
}

   


  buildRequestJson() {
    // fill resBody with data from Form
    //console.log(this.newForm.value);
   // this.getCheckBox();
  this.layerString = this.ConvertLayerArrJsonToString();
  var affectedServiceString:string[] = this.ConvertServiceArrJsonToString();
    this.selectedenv = this.newForm.get('Environment').value;
  //  this.layers = this.newForm.get('layers').value;
    this.selectedAffectedService = this.newForm.get('affectedService').value;
    this.resBody.environment = this.selectedenv;							  
    // this.resBody.layer = this.selectedLayer;
    this.resBody.layer = this.layerString;
    //this.resBody.affectedService = this.selectedAffectedService;
    this.resBody.affectedService = affectedServiceString;
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

    this.addRequestInfo(this.id, this.displayName, this.newForm.get('deploymentComment').value);
     this.resBody.requestInfo = this.requestInfoArr;
    console.log(JSON.stringify(this.resBody));
    this.resBody.assignOnUser = this.assignOnUser;
    this.resBody.pickedByUser = this.pickedByUser;
     
    
   // console.log("res body: "+JSON.stringify(this.resBody));
  }

  onSubmit() {
    this.submitted=true;
    this.buildRequestJson();
    console.log(this.requestInfoArr);

    if (this.newForm.invalid) {
      return;
  }

    this.searchService.newRequest(this.resBody).
      subscribe(
        (data: Request) => {
          if (data.responseStatus.statusCode === 200) {
            alert('Request submitted successfully.');
            this.router.navigate(['home']);
          } else {
            alert(data.responseStatus);
            this.router.navigate(['home']);
          }
        },
        error => {
          alert("Invalid request Please try again.");
          this.router.navigate(['home']);
        });
  }

  get f() { return this.newForm.controls; }


  // getCheckBox() {
  //   if (this.WPS === true) {
  //     this.selectedLayer.push("WPS");
  //     console.log(" WPS True: ");

  //   }
  //   if (this.IIB === true) {
  //     this.selectedLayer.push("IIB");
  //     console.log(" IIB True: ");

  //   }
  //   if (this.WSRR === true) {
  //     this.selectedLayer.push("WSRR");
  //     console.log(" WSRR True: ");

  //   }
  //   if (this.WAS === true) {
  //     this.selectedLayer.push("WAS");
  //     console.log(" WAS True: ");

  //   }
  //   if (this.DP === true) {
  //     this.selectedLayer.push("DP");
  //     console.log(" DP True: ");

  //   }
  //   if (this.DB === true) {
  //     this.selectedLayer.push("DB");
  //     console.log(" DB True: ");


  //   }
  //   if (this.ODM === true) {
  //     console.log(" ODM True: ");

  //     this.selectedLayer.push("ODM");

  //   }
  //   console.log(" layers log: " + this.selectedLayer);

  // }


  onItemSelect(selectedItem : any){

    this.selectedItems.push(selectedItem);
    this.selectedItems.pop();

  } 

  onItemDeSelect(item :any){
    for(var i=0;i<this.selectedItems.length;i++){
      if(this.selectedItems[i]==item){
        this.selectedItems.splice(i,1);
      }
    }
 }
 
  onItemSelectLayer(selectedItem : any){

  this.selectedItems.push(selectedItem);
  this.selectedItems.pop();

} 

onItemDeSelectLayer(item :any){
  for(var i=0;i<this.selectedItems.length;i++){
    if(this.selectedItems[i]==item){
      this.selectedItems.splice(i,1);
    }
  }
}

 
									   

										


  toggleVisibility(e: any) {
    this.marked = e.target.checked;

  }
  logout() {
    // remove user from local storage to log user out
    // this.router.navigate(['login']);
    // localStorage.removeItem('user');
    
    this.authenticationService.logout();
}

}
