
import { TextField, PasswordField, EmailField, BooleanField, DateField, register } from 'rev-models';
import * as forms from 'rev-forms';
// import * as api from 'rev-api-client';

export default class User {
    @TextField('Username')
        username: string;
    @PasswordField('Password')
        password: string;
    @EmailField('Email', {required: false})
        email: string;
    @BooleanField('Active?')
        active: boolean;
    @DateField('Last Login')
        last_login: Date;
}

register(User);

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
