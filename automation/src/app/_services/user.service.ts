import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { User } from '../_models';
import { UserBody } from '../_models/UserBody';
import { UserCredentials } from '../_models/userCredentials';
import { Observable } from 'rxjs';

@Injectable()
export class UserService {
    constructor(private http: HttpClient) { }





    login(userCred: UserCredentials): Observable<any> {
        return this.http.post(`http://localhost:8090/api/login` , userCred);
    }


    getAll() {
        return this.http.get<User[]>(`${config.apiUrl}/users`);
    }


    getById(id: number) {
        return this.http.get(`${config.apiUrl}/users/` + id);
    }

    register(userBody: UserBody) {
        console.log("here------------- register");
        return this.http.post(`http://localhost:8090/api/NewUser`, userBody);
    }

    update(user: User) {
        return this.http.put(`${config.apiUrl}/users/` + user.userId, user);
    }

    delete(id: number) {
        return this.http.delete(`${config.apiUrl}/users/` + id);
    }
}