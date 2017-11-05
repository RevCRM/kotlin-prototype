import * as React from 'react';

import Paper from 'material-ui/Paper';

export function Dashboard() {
    return (
        <div className="row" style={{paddingTop: 50}}>
            <div className="col-xs-11 col-md-8">
                <Paper zDepth={2}>

                    <h2>Dashboard</h2>

                    <p>Cool stuff goes here...</p>

                </Paper>
            </div>
        </div>
    );
}
