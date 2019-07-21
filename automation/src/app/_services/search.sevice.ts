import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Request, Status } from '../_models';
import { map } from 'rxjs/operators';
import { Observable } from "rxjs";
import { ResponseBody } from '../_models/responseBody';



@Injectable()
export class SearchService {

    obj: object[];
    private _url = 'https://desolate-depths-48453.herokuapp.com/api/';

    constructor(private http: HttpClient) { }


    newRequest(req: ResponseBody) {
        req.status = 'SENT'
        return this.http.post('http://localhost:8086/api/addDeploymentRequest', req);

    }

    updateRequestObj(req: ResponseBody) {
        return this.http.put('http://localhost:8086/api/updateDeploymentRequest', req);
    }

    updateRequest(reqId: String, newStatus: string): Observable<any> {
      // var myDate ='2019-12-1-12:20:11';
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

    updateRequestData(reqId: String, newStatus: string, requestSubject : string): Observable<any> {
        // var myDate ='2019-12-1-12:20:11';
          return this.http.put('http://localhost:8086/api/UpdateDeploymentRequestStatusSubject/' + reqId+ '/' + newStatus + '/' + requestSubject,
              {
                  headers: new HttpHeaders()
                      .append('Access-Control-Allow-Origin', '*')
                      .append('Content-Type', 'application/json')
                      .append('Accept', 'application/json')
                      .append('Access-Control-Allow-Methods', 'POST, GET, OPTIONS, DELETE, PUT, PATCH')
                      .append('Access-Control-Allow-Headers', "X-Requested-With, Content-Type, Origin, Authorization, Accept, Client-Security-Token, Accept-Encoding")
              });
      }

    getNowDate() {
        //return string
        var returnDate = "";
        //get datetime now
        var today = new Date();
        //split
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //because January is 0! 
        var yyyy = today.getFullYear();
        //Interpolation date
        if (dd < 10) {
            returnDate += `0${dd}.`;
        } else {
            returnDate += `${dd}.`;
        }
    
        if (mm < 10) {
            returnDate += `0${mm}.`;
        } else {
            returnDate += `${mm}.`;
        }
        returnDate += yyyy;
        return returnDate;
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

    getAllowedStatusesList(currentStatus: String, assignedGroup: String){
        return this.http.get<Status[]>('http://localhost:8086/api/AllowedStatusesList/' + currentStatus + '/' + assignedGroup);
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