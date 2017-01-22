import * as React from 'react';
import * as ReactDOM from 'react-dom';

import { Router, browserHistory } from 'react-router';

import Routes from '../components/Routes';

ReactDOM.render((
    <Router history={browserHistory}>
        {Routes}
    </Router>
    ),
    document.getElementById('app')
);
