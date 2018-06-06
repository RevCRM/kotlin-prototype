import * as React from 'react';

import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { TopNav } from './menu/TopNav';
import { LeftNav } from './menu/LeftNav';
import { LoginPage } from './login/LoginPage';
import { Route } from 'react-router-dom';
import { CRMViewManager } from './views/CRMViewManager';

const theme = createMuiTheme();

export class App extends React.Component {

    componentDidMount() {
        console.log('mounted context', this.context);
    }

    // render={() => <Redirect to="/accounts/list" />}
    render() {
        return (
            <MuiThemeProvider theme={theme}>
                <CssBaseline />
                <LeftNav />
                <TopNav />
                <Route path="/login" component={LoginPage} />
                <Route path="/:perspectiveName/:viewName" component={CRMViewManager} />
            </MuiThemeProvider>
        );
    }
}
