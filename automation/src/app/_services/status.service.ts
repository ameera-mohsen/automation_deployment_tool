import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Status } from '../_models';

@Injectable()
export class StatusService {
    constructor (private http : HttpClient) {}

    getAll() {
        return this.http.get<Status[]>('https://pacific-brook-25356.herokuapp.com/api/Status');
    }
}

