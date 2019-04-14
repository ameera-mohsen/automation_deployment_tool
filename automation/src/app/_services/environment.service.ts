import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Environment } from '../_models';

@Injectable()
export class EnvironmentService {
    constructor (private http : HttpClient) {}
    getAll() {
        return this.http.get<Environment[]>(`https://pacific-brook-25356.herokuapp.com/api/Enviroments`);
    }

}