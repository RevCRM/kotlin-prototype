
import * as rev from 'rev-models';

// User Login Form Fields

export class UserLogin extends rev.Model {

    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;

    login() {
        console.log('UserLogin.login called', this, arguments);
    }
}
