import { UserLogin } from '../UserLogin';
import { IMethodContext } from 'rev-models';

export class UserLoginBackend extends UserLogin {

    async login(ctx: IMethodContext<any>) {
        const validation = await ctx.manager.validate(this);
        if (!validation.valid) {
            throw new Error('model invalid');
        }
        console.log('UserLoginBackend.login called', this);
    }

}
