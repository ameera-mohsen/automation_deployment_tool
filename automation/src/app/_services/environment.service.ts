import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Environment } from '../_models';

@Injectable()
export class EnvironmentService {
    constructor (private http : HttpClient) {}
    getAll() {
        return this.http.get<Environment[]>(`http://localhost:9030/api/Enviroments`);
    }

}