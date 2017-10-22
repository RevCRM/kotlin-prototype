import { UserLogin as BaseUserLogin } from '../UserLogin';
import { IMethodContext } from 'rev-models';
import { ApiMethod } from 'rev-api';

export class UserLogin extends BaseUserLogin {

    @ApiMethod({ modelData: true })
    async login(ctx: IMethodContext<any>) {
        console.log('UserLoginBackend.login called with model', this);
    }

}
