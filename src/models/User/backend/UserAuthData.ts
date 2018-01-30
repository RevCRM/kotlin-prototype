
import * as rev from 'rev-models';
import { EntityModel } from '../../BaseModels';

export class UserAuthData extends EntityModel<UserAuthData> {

    @rev.IntegerField()
        userId: number;
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
    @rev.DateField({ label: 'Last Login', required: false })
        last_login: Date;

}
