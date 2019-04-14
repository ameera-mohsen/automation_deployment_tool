import { ResponseStatus } from "./responseStatus";
import { Groups } from "./groups";
import { UserCredentials } from "./userCredentials";
import { Privilege } from "./privilege";
import { UserBody } from "./UserBody";

export interface UserCredentialsResBody {
    responseStatus: ResponseStatus;
    responseBody: UserBody;
}