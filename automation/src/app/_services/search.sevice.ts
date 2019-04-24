import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Request } from '../_models';
import { map } from 'rxjs/operators';
import { Observable } from "rxjs";
import { ResponseBody } from '../_models/responseBody';



@Injectable()
export class SearchService {

    obj: object[];
    private _url = 'https://desolate-depths-48453.herokuapp.com/api/';

    constructor(private http: HttpClient) { }


    newRequest(req: ResponseBody) {
        return this.http.post('http://localhost:8086/api/addDeploymentRequest', req);

    }

    updateRequestObj(req: ResponseBody) {
        return this.http.put('http://localhost:8086/api/updateDeploymentRequest', req);
    }

    updateRequest(reqId: String, newStatus: string): Observable<any> {
        return this.http.put('http://localhost:8086/api/UpdateDeploymentRequestStatus/' + reqId + '/' + newStatus,
            {
                headers: new HttpHeaders()
                    .append('Access-Control-Allow-Origin', '*')
                    .append('Content-Type', 'application/json')
                    .append('Accept', 'application/json')
                    .append('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE, PUT, PATCH')
                    .append('Access-Control-Allow-Headers', "X-Requested-With, Content-Type, Origin, Authorization, Accept, Client-Security-Token, Accept-Encoding")
            });
    }

    getAllRequests(): Observable<any> {
        return this.http.get('http://localhost:8086/api/DeploymentRequests');
    }

    getDeploymentReqByInitiatorUserId(userId: String): Observable<any>{
        return this.http.get('http://localhost:8086/api/DeploymentReqByInitiatorUserId/'+userId);

    }

    getDeploymentReqByAssignedUserId(userId: String): Observable<any>{
        return this.http.get('http://localhost:8086/api/DeploymentReqByAssignedUserId/'+userId);

    }



    searchRequestById(id: String): Observable<any> {
        let headers: HttpHeaders = new HttpHeaders();
        headers = headers.append('Access-Control-Allow-Origin', '*');
        console.log(" Param " + id)
        return this.http.get(`http://localhost:8086/api/DeploymentRequestById/` + id);
    }

    searchRequestByInitiatorUserId(userId: string): Observable<any> {
        return this.http.get(`http://localhost:8086/api/DeploymentReqByInitiatorUserId/` + userId);
    }

    searchReqestByAssignedUserId(userId: string): Observable<any> {
        return this.http.get(`http://localhost:8086/api/DeploymentReqByAssignedUserId/` + userId);
    }

    searchRequestsByCriteria(params: HttpParams): Observable<any> {
        console.log("Param::>>> " + params);
        let headers: HttpHeaders = new HttpHeaders();
        //headers = headers.append('Access-Control-Allow-Origin', '*');
        return this.http.get('http://localhost:8086/api/searchDeploymentRequestByCriteria',
        {params: params});
    }



}