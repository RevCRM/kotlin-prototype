import * as React from 'react';
import * as ReactDOM from 'react-dom';

import { Provider } from 'react-redux';
import { Router, browserHistory } from 'react-router';
import { getStore } from '../store/index';

import Routes from '../components/Routes';
import '../components/models';

// Required for onTouchTap event
import * as injectTapEventPlugin from 'react-tap-event-plugin';
injectTapEventPlugin();

const store = getStore();

ReactDOM.render((
        <Provider store={store} >
            <Router history={browserHistory}>
                {Routes}
            </Router>
        </Provider>
    ),
    document.getElementById('app')
);
