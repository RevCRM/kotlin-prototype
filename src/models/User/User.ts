
import * as rev from 'rev-models';
// import * as forms from 'rev-forms';
// import * as api from 'rev-api-client';

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
}

/*
rev.register(User, {
    validate: (model, operation, result) => {
        if (model.username == 'Bryan') {
            result.addModelError('Bryan is not allowed!');
        }
    }
});
*/
