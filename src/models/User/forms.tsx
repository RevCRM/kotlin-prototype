
import * as React from 'react';
import { RevForm, RevField } from 'rev-forms-redux-mui';

export const LoginForm = () => (
    <RevForm model="User" form="login_form">
        <RevField name="username" />
        <RevField name="password" />
        <RevField name="active" />
        <RevField name="user_type" />
        <RevField name="age" />
    </RevForm>
);
