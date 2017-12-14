import * as React from 'react';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import { TopNav } from './menu/TopNav';
import { LeftNav } from './menu/LeftNav';
import { LoginPage } from './login/LoginPage';
import { Route } from 'react-router-dom';
import { ViewManager } from './views/ViewManager';

export function App(props: any) {
    return (
        <MuiThemeProvider>
            <div className="revcrm">
                <LeftNav />
                <TopNav />
                <div className="revcrm-content">
                    <Route exact path="/" component={ViewManager} />
                    <Route path="/login" component={LoginPage} />
                </div>
            </div>
        </MuiThemeProvider>
    );
}
