
import * as rev from 'rev-models';

export default class User {
    username: string;
    password: string;
    email: string;
    active: boolean;
    lastLogin: Date;
}

rev.register(User, {
    fields: [
        new rev.TextField('username', 'User Name'),
        new rev.PasswordField('password', 'Password'),
        new rev.PasswordField('password_repeat', 'Password', {stored: false}),
        new rev.EmailField('email', 'Email Address', {required: false}),
        new rev.BooleanField('active', 'Active?'),
        new rev.DateField('lastLogin', 'Last Login')
    ]
});
