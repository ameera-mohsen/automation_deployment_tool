import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Layer } from '../_models';

@Injectable()
export class LayersService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get<Layer[]>(`http://localhost:9030/api/Layers`);
    }
}