import { Groups } from "./groups";
import { Privilege } from "./privilege";
import { UserCredentials } from "./userCredentials";



export interface UserBody {

    _id: string;
    displayName: string;
    email: string;
    number: string;
    userStatus: string;
    credentials: UserCredentials;
    privilege: Privilege;
    groups: Groups;

}