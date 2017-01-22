import * as React from 'react';

import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import TopNav from './menu/TopNav';
import LeftNav from './menu/LeftNav';

export default function App(props: any) {
    return (
        <MuiThemeProvider>
            <div className="revcrm">
                <LeftNav />
                <TopNav />
                <div className="revcrm-content">
                    {props.children}
                </div>
            </div>
        </MuiThemeProvider>
    );
}
