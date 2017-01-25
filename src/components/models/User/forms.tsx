
import * as React from 'react';
import { RevForm, RevField } from 'rev-forms-redux-mui';

export const LoginForm = () => (
    <RevForm model="User" form="login_form">
        <RevField name="username" />
        <RevField name="password" />
        <RevField name="last_login" />
        <RevField name="active" />
    </RevForm>
);
