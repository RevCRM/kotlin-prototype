import * as React from 'react';

import Paper from 'material-ui/Paper';
import RaisedButton from 'material-ui/RaisedButton';
import Divider from 'material-ui/Divider';
import TextField from 'material-ui/TextField';

export default function LoginPage() {
    return (
        <div className="revcrm-login row nomargin center-xs" style={{paddingTop: 50}}>
            <div className="col-xs-11 col-md-5">
                <Paper zDepth={2} className="box">

                    <div style={{background: '#00F'}}>
                        <h1 style={{background: 'rgb(0, 188, 212)', color: '#FFF'}}>Welcome!</h1>
                    </div>

                    <Divider />

                    <div style={{padding: 20}}>
                        <TextField hintText="Username" fullWidth={true} />
                        <TextField hintText="Password" fullWidth={true} type="password" />
                        <RaisedButton label="Log In" primary={true} style={{marginTop: 15}} />
                    </div>
                </Paper>
            </div>
        </div>
    );
}
