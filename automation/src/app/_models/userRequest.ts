//import { User } from "./user";
//import {Comment} from "./comment"
import { ResponseStatus } from "./responseStatus";
import { UserBody } from "./UserBody";

export interface UserRequest {
    responseStatus: ResponseStatus;
    responseBody: UserBody[];

}