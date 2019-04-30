
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Service } from '../_models';


@Injectable()
export class ServiceListService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<Service[]>(`http://localhost:9030/api/Services`);
    }

    

}