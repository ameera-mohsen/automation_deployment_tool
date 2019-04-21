import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Status } from '../_models';

@Injectable()
export class StatusService {
    constructor (private http : HttpClient) {}

    getAll() {
        return this.http.get<Status[]>('http://localhost:9030/api/Status');
    }
}

