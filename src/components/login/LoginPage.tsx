import * as React from 'react';

import Paper from 'material-ui/Paper';
import Divider from 'material-ui/Divider';
import { UserLoginForm } from '../../models/User/forms/UserLoginForm';
import { StyleRules, withStyles, WithStyles } from 'material-ui/styles';
import Typography from 'material-ui/Typography';

const styles: StyleRules = {
    root: {
        marginTop: 100,
        display: 'flex',
        justifyContent: 'center'
    },
    loginTitle: {
        margin: 20
    },
    loginBox: {
        maxWidth: 350
    }
};

function LoginPageC(props: WithStyles) {
    return (
        <div className={props.classes.root}>
            <Paper elevation={4} className={props.classes.loginBox}>

                <Typography align="center" type="display2" className={props.classes.loginTitle}>Login</Typography>

                <Divider />

                <UserLoginForm />

            </Paper>
        </div>
    );
}

export const LoginPage = withStyles(styles)(LoginPageC);
