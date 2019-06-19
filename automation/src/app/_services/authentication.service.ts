import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {Router} from "@angular/router";

@Injectable()
export class AuthenticationService {
    constructor(private router: Router ,private http: HttpClient) { }

    login(username: string, password: string) {
        return this.http.post<any>(`${config.apiUrl}/users/authenticate`, { username: username, password: password })
            .pipe(map(user => {
                // login successful if there's a jwt token in the response
                if (user && user.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }

                return user;
            }));
    }

    logout() {
        // remove user from local storage to log user out
        this.router.navigate(['login']);
        localStorage.removeItem('user');
        window.localStorage.removeItem("user");
        window.localStorage.removeItem("id");
        window.localStorage.removeItem("displayName");
        window.localStorage.removeItem("email");
        window.localStorage.removeItem("group");
        window.localStorage.removeItem("editReqId");
        window.localStorage.removeItem("viewReqId");
        
        
       
    }

    
}