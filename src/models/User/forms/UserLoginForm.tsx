
import * as React from 'react';
import * as rev from 'rev-models';
import { ModelForm, ModelField, ModelAction } from 'rev-forms-materialui';

export class UserLoginFormModel {
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
}

export const UserLoginForm = () => (
    <ModelForm model="UserLoginFormModel">
        <ModelField name="username" />
        <ModelField name="password" />
        <ModelAction type="post" url="/login" label="Log In" onSuccess={() => alert('yay!')} />
    </ModelForm>
);
