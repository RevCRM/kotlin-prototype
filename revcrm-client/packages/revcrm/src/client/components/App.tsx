import * as React from 'react';

import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import { TopNav } from './menu/TopNav';
import { LeftNav } from './menu/LeftNav';
import { Routes } from './Routes';

const theme = createMuiTheme();

export class App extends React.Component {

    componentDidMount() {
        console.log('mounted context', this.context);
    }

    render() {
        return (
            <MuiThemeProvider theme={theme}>
                <CssBaseline />
                <LeftNav />
                <TopNav />
                <Routes />
            </MuiThemeProvider>
        );
    }
}
