import * as React from 'react';
import * as PropTypes from 'prop-types';

import { MuiThemeProvider, createMuiTheme } from 'material-ui/styles';
import { TopNav } from './menu/TopNav';
import { LeftNav } from './menu/LeftNav';
import { LoginPage } from './login/LoginPage';
import { Route } from 'react-router-dom';
import { ViewManager } from './views/ViewManager';

const theme = createMuiTheme();

export class App extends React.Component {

    static contextTypes = {
        modelManager: PropTypes.object
    };

    componentDidMount() {
        console.log('mounted context', this.context);
    }

    render() {
        return (
            <MuiThemeProvider theme={theme}>
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
}
