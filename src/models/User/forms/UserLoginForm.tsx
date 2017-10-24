
import * as React from 'react';
import * as rev from 'rev-models';
import { ModelForm, ModelField, ModelAction } from 'rev-forms-redux-mui';

export class UserLoginFormModel {
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
}

export const UserLoginForm = () => (
    <ModelForm model="UserLoginModel" form="login_form">
        <ModelField name="username" />
        <ModelField name="password" />
        <ModelAction label="Log In" method="login" args={{arg1: 'argTest', arg2: 22}} />
        <ModelAction label="Log Out" method="logout" />
    </ModelForm>
);
