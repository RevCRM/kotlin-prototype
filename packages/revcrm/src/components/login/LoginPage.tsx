import * as React from 'react';

import Paper from 'material-ui/Paper';
import Divider from 'material-ui/Divider';
import { UserLoginForm } from '../../models/User/forms/UserLoginForm';
import { withStyles, WithStyles } from 'material-ui/styles';
import Typography from 'material-ui/Typography';

const styles = {
    root: {
        marginTop: 100,
        display: 'flex',
        justifyContent: 'center'
    },
    loginTitle: {
        margin: 20
    },
    loginBox: {
        width: '100%',
        maxWidth: 350
    }
};

class LoginPageC extends React.Component<WithStyles<keyof typeof styles>> {
    render() {
        return (
            <div className={this.props.classes.root}>
                <Paper elevation={4} className={this.props.classes.loginBox}>

                    <Typography align="center" variant="display2" className={this.props.classes.loginTitle}>Login</Typography>

                    <Divider />

                    <UserLoginForm />

                </Paper>
            </div>
        );
    }
}

export const LoginPage = withStyles(styles as any)(LoginPageC) as any;
