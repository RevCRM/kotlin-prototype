
import * as React from 'react';
import { ModelForm, ModelField, ModelAction } from 'rev-forms-redux-mui';

export const UserLoginForm = () => (
    <ModelForm model="UserLogin" form="login_form">
        <ModelField name="username" />
        <ModelField name="password" />
        <ModelAction method="login" label="Log In" />
        <ModelAction method="logout" label="Log Out" />
    </ModelForm>
);
