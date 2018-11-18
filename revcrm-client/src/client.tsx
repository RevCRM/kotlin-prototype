
import * as React from 'react';
import * as ReactDOM from 'react-dom';
import { GoogleAuth } from './components/auth/GoogleAuth';

ReactDOM.render(
    (<>
        <h2>RevCRM</h2>
        <GoogleAuth />
    </>),
    document.getElementById('app')
);
