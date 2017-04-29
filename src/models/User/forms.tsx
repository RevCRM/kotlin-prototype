
import * as React from 'react';
import { ModelForm, ModelField } from 'rev-forms-redux-mui';

export const LoginForm = () => (
    <ModelForm model="User" form="login_form">
        <ModelField name="username" />
        <ModelField name="password" />
    </ModelForm>
);
