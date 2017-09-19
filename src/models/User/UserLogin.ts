
import * as rev from 'rev-models';

// User Login Form Fields

export class UserLogin {

    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;

}
