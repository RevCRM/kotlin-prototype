
import * as rev from 'rev-models';
// import * as forms from 'rev-forms';
// import * as api from 'rev-api-client';

const TYPES = [
    ['normal', 'Normal User'],
    ['admin', 'Administrator']
];

export default class User {
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
    @rev.NumberField({ label: 'Age' })
        age: number;
    @rev.EmailField({ label: 'Email', required: false})
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;
    @rev.DateField({ label: 'Last Login' })
        last_login: Date;
    @rev.SelectionField({ label: 'User Type', selection: TYPES })
        user_type: string;
}

rev.register(User, {
    validate: (model, operation, result) => {
        if (model.username == 'Bryan') {
            result.addModelError('Bryan is not allowed!');
        }
    }
});
