
import * as rev from 'rev-models';

interface IValidationContext {
    operation: any;
    result: any;
}

export class User extends rev.Model {
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
    @rev.EmailField({ label: 'Email', required: false })
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;
    @rev.DateField({ label: 'Last Login', required: false })
        last_login: Date;

    validate(vc: IValidationContext) {
        if (this.username == 'bryan') {
            vc.result.addModelError('Username is not valid');
        }
    }
}
