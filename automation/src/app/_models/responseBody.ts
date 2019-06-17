import { User } from "./user";
import {RequestInfo} from "./requestInfo"


export interface ResponseBody {
    id: string;
    status: string;
    environment: string;
    defectId: string;
    assignOnGroup: string;
    requestDate: Date;
    deploymentTime: Date;
    reason: string;
    releaseNote: string;
    requestSubject: string;
    layer: string[];
    affectedService: string[];
    requestInfo: RequestInfo[];
    initiatorUser: User;
    assignOnUser: User;
    pickedByUser: User;
}