//import { User } from "./user";
//import {Comment} from "./comment"
import { ResponseStatus } from "./responseStatus";
import { ResponseBody } from "./responseBody";

export interface Request {
    responseStatus: ResponseStatus;
    responseBody: ResponseBody[];

}