
import * as rev from 'rev-models';
// import * as forms from 'rev-forms';
// import * as api from 'rev-api-client';

const TYPES = [
    ['normal', 'Normal User'],
    ['admin', 'Administrator']
];

export default class User {
    @rev.TextField('Username')
        username: string;
    @rev.PasswordField('Password')
        password: string;
    @rev.NumberField('Age')
        age: number;
    @rev.EmailField('Email', {required: false})
        email: string;
    @rev.BooleanField('Active?')
        active: boolean;
    @rev.DateField('Last Login')
        last_login: Date;
    @rev.SelectionField('User Type', TYPES)
        user_type: string;
}

rev.register(User, {
    validate: (model, operation, result) => {
        if (model.username == 'Bryan') {
            result.addModelError('Bryan is not allowed!');
        }
    }
});
