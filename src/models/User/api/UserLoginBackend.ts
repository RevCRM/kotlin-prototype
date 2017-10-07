import { UserLogin } from '../UserLogin';
import { IMethodContext } from 'rev-models';
import { ApiMethod } from 'rev-api';

export class UserLoginBackend extends UserLogin {

    @ApiMethod({ validateModel: true })
    async login(ctx: IMethodContext<any>) {
        console.log('UserLoginBackend.login called with valid model', this);
    }

}
