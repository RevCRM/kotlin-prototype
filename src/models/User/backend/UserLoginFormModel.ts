import { UserLoginFormModel as ClientUserLoginFormModel } from '../forms/UserLoginForm';
import { IMethodContext } from 'rev-models';
import { ApiMethod } from 'rev-api';

export class UserLoginFormModel extends ClientUserLoginFormModel {

    @ApiMethod({ modelData: true })
    async login(ctx: IMethodContext<any>) {
        console.log('UserLoginBackend.login called with model', this);
    }

}
