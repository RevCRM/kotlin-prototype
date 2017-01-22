import * as React from 'react';
import { IndexRoute, Route } from 'react-router';

import App from './App';
import LoginPage from './login/LoginPage';

const routes = (
    <Route path="/" component={App}>
        <IndexRoute component={LoginPage} />
        <Route path="login" component={LoginPage} />
    </Route>
);

export default routes;
