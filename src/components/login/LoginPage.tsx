import * as React from 'react';

import Paper from 'material-ui/Paper';
import Divider from 'material-ui/Divider';

import { RevForm, RevField } from 'rev-forms-redux-mui';

export default function LoginPage() {
    return (
        <div className="revcrm-login row nomargin center-xs" style={{paddingTop: 50}}>
            <div className="col-xs-11 col-md-5">
                <Paper zDepth={2} className="box">

                    <div style={{background: '#00F'}}>
                        <h1 style={{background: 'rgb(0, 188, 212)', color: '#FFF'}}>Welcome!</h1>
                    </div>

                    <Divider />

                    <RevForm model="User" form="login_form">
                        <RevField name="user" />
                    </RevForm>

                </Paper>
            </div>
        </div>
    );
}
