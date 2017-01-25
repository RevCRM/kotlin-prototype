
import * as models from 'rev-models';
import * as forms from 'rev-forms';
// import * as api from 'rev-api-client';

export default class User {
    username: string;
    password: string;
    email: string;
    active: boolean;
    last_login: Date;
}

models.register(User, {
    fields: [
        new models.TextField('username', 'User Name'),
        new models.PasswordField('password', 'Password'),
        new models.EmailField('email', 'Email Address', {required: false}),
        new models.BooleanField('active', 'Active?'),
        new models.DateField('last_login', 'Last Login'),
    ]
});

forms.register(User, 'login_form', {
    title: 'Log In',
    fields: [
        'username',
        'password'
    ] /*,
    actions: {
        login: {
            label: 'Log In',
            validateFirst: true,
            method: (model: any) => {
                api.call(model, 'do_login');
            }
        }
    }*/
});
