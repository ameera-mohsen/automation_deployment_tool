import { Component, OnInit, ViewChild, Input, Output, EventEmitter } from '@angular/core';
import { first, isEmpty } from 'rxjs/operators';
import {Router} from "@angular/router";
import { User } from '../_models';
import { UserService } from '../_services';
import { Layer } from '../_models';
import { Status } from '../_models';
import { Service } from '../_models';
import { Environment } from '../_models';
import { Request } from '../_models';
import { LayersService } from '../_services';
import { StatusService , AuthenticationService} from '../_services';
import { ServiceListService } from '../_services';
import { EnvironmentService } from '../_services';
import { SearchService } from '../_services';
import { environment } from '../../environments/environment.prod';
import { HttpParams } from '@angular/common/http';
import { Console } from '@angular/core/src/console';
import { ResponseBody } from '../_models/responseBody';
import { ResponseStatus } from '../_models/responseStatus';
import { HttpParamsOptions } from '@angular/common/http/src/params';
import { Subscription } from 'rxjs';
import { UserCredentialsResBody } from '../_models/userCredentialsResBody';
import { RequestInfo} from '../_models';

@Component({
    selector: 'home',
    templateUrl: 'home.component.html',
    styleUrls: ['../css/Admin.min.css', '../css/bootstrap.min.css', '../css/_all-skins.min.css']
})

export class HomeComponent implements OnInit {

    @Input() count = 0;
    @Output() countChange = new EventEmitter<number>();
    

    increment() {
        this.count++;
        this.countChange.emit(this.count);
    }
    
    id: string;
    displayName: string;
    email: string;
    group : string;
    requestInfo: RequestInfo[];
    reqInfo = {} as RequestInfo;
    currentUser: User;
    allRequestBody: ResponseBody[];
    resBody = {} as ResponseBody;
    resStatus = {} as ResponseStatus;
    status: object[];
    users: User[] = [];
    layers: Layer[] = [];
    statuses: Status[] = [];
    services: Service[] = [];
    environments: Environment[] = [];
    requestNumber: string = '';
    requestOwner: string = '';
    selectedService: string = '';
    selectedEnv: string = '';
    selectedStatus: string = '';
    selectedLayer: string = '';
    public searchText : string;
    options: HttpParamsOptions = {} as HttpParamsOptions
    constructor( private router: Router, private userService: UserService, private layerService: LayersService,
        private statusService: StatusService, private serviceListService: ServiceListService,
        private environmentService: EnvironmentService,private authenticationService: AuthenticationService, private searchService: SearchService ) {
       // this.currentUser = JSON.parse(localStorage.getItem('currentUser'));

    }
    loadingSubscription: Subscription;
    assignedUserId: String = ''; 
    
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
        this.group = window.localStorage.getItem("group");
 
        this.loadLayers();
        this.loadStatuses();
        this.loadServices();
        this.loadEnvironments();
        this.loadAllDeployments();
        
    }

    

    deleteUser(id: number) {
        this.userService.delete(id).pipe(first()).subscribe(() => {
            this.loadAllUsers()
        });
    }

    private loadAllUsers() {
        this.userService.getAll().pipe(first()).subscribe(users => {
            this.users = users;
        });
    }

    private loadLayers() {
        this.layerService.getAll().pipe(first()).subscribe(layers => {
            this.layers = layers;
        });
    }

    private loadStatuses() {
        this.statusService.getAll().pipe(first()).subscribe(statuses => {
            this.statuses = statuses;
        });
    }

    private loadServices() {
        this.serviceListService.getAll().pipe(first()).subscribe(services => {
            console.log('search --------------------------');
            this.services = services;
        });
    }

    private loadEnvironments() {
        this.environmentService.getAll().pipe(first()).subscribe(environments => {
            this.environments = environments;
            //console.log(this.environments);
        });

    }

    SearchRequests() {
        let params = new HttpParams();
        console.log('Search Request..');
        
        console.log(this.requestNumber);

        if (this.selectedLayer) {
            params = params.append('Layer', this.selectedLayer);
        }
        if (this.selectedEnv) {
            params = params.append('Environment', this.selectedEnv);
        }
        if (this.selectedService) {
            params = params.append('AffectedService', this.selectedService);
        }
        if (this.selectedStatus) {
            params = params.append('Status', this.selectedStatus);
        }
        if (this.requestNumber) {
            params = params.append('id', this.requestNumber);
        }
        console.log('requestOwner -----:: >> ' +this.requestOwner);
        if (this.requestOwner) {
            params = params.append('initiatorUser.displayName', this.requestOwner);
        }
        
        
        console.log('Search Request.. Parameters:: >> ' + params.getAll);

        this.searchService.searchRequestsByCriteria(params).subscribe((data: Request) => {
         
         this.allRequestBody = data.responseBody;
      
        });
    }
    loadAllDeployments() {
        console.log('Load deployments related to....... ' + this.id);
        let res = this.searchService.getDeploymentReqByAssignedUserId(this.id).subscribe((data: Request) => {
            this.allRequestBody = data.responseBody;
            // console.log(' <<<<<< loadAllDeployments.id  >>>>>> ' + this.resBody.id );
        });
    }

    editRequest(req: ResponseBody): void{
        window.localStorage.removeItem("editReqId");
        window.localStorage.setItem("editReqId", req.id.toString());
        this.router.navigate(['edit']);

    }

    pickRequest(req: ResponseBody): void{
        window.localStorage.removeItem("editReqId");
        window.localStorage.setItem("editReqId", req.id.toString());
        this.router.navigate(['edit']);

    }
    logout() {
        // remove user from local storage to log user out
        // this.router.navigate(['login']);
        // localStorage.removeItem('user');
        this.authenticationService.logout();
       
    }

  
}