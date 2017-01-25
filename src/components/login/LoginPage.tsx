import * as React from 'react';

import Paper from 'material-ui/Paper';
import Divider from 'material-ui/Divider';
import { LoginForm } from '../models/User/forms';

export default function LoginPage() {
    return (
        <div className="revcrm-login row nomargin center-xs" style={{paddingTop: 50}}>
            <div className="col-xs-11 col-md-5">
                <Paper zDepth={2} className="box">

                    <div style={{background: '#00F'}}>
                        <h1 style={{background: 'rgb(0, 188, 212)', color: '#FFF'}}>Welcome!</h1>
                    </div>

                    <Divider />

                    <LoginForm />

                </Paper>
            </div>
        </div>
    );
}
