import * as React from 'react';

import RaisedButton from 'material-ui/RaisedButton';

export default function LoginPage() {
    return (
        <div className="row center-xs">
            <div className="col-xs-6">
                <div className="box">
                    <h1>Log In</h1>
                    <RaisedButton label="Log In" />
                </div>
            </div>
        </div>
    );
}
