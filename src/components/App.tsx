import * as React from 'react';

import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';
import { TopNav } from './menu/TopNav';
import { LeftNav } from './menu/LeftNav';
import { LoginPage } from './login/LoginPage';
import { Route, Redirect } from 'react-router-dom';
import { CRMViewManager } from './views/CRMViewManager';

const theme = createMuiTheme();

export class App extends React.Component {

    componentDidMount() {
        console.log('mounted context', this.context);
    }

    render() {
        return (
            <MuiThemeProvider theme={theme}>
                <LeftNav />
                <TopNav />
                <Route path="/login" component={LoginPage} />
                <Route path="/:perspective/:view" component={CRMViewManager} />
                <Route exact path="/" render={() => <Redirect to="/accounts/list" />} />
            </MuiThemeProvider>
        );
    }
}
