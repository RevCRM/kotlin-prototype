
import * as React from 'react';
import * as rev from 'rev-models';
import { DetailView, Field, PostAction } from 'rev-ui';
import Dialog from '@material-ui/core/Dialog';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import DialogActions from '@material-ui/core/DialogActions';
import Grid from '@material-ui/core/Grid';
import Button from '@material-ui/core/Button';
import { withStyles, WithStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';

export class UserLoginFormModel {
    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'Password' })
        password: string;
}

export interface IUserLoginFormState {
    dialogOpen: boolean;
    dialogTitle: string;
    dialogMessage: string;
}

const styles = {
    root: {
        margin: 12
    }
};

class UserLoginFormC extends React.Component<WithStyles<keyof typeof styles>, IUserLoginFormState> {

    constructor(props: any) {
        super(props);
        this.state = {
            dialogOpen: false,
            dialogTitle: '',
            dialogMessage: ''
        };
    }

    dialogClose() {
        this.setState({ dialogOpen: false });
    }

    loginFailed(err: Response | Error) {
        this.setState({
            dialogOpen: true,
            dialogTitle: 'Login Failed',
            dialogMessage: 'Please check your entries and try again.'
        });
    }

    render() {
        return (
            <div className={this.props.classes.root}>
                <DetailView model="UserLoginFormModel">
                    <Grid container spacing={8}>
                        <Field name="username" colspan={12} />
                        <Field name="password" colspan={12} />
                    </Grid>

                    <PostAction
                        label="Log In"
                        url="/login"
                        onResponse={(res) => {
                            if (res.status == 200) {
                                window.location.pathname = '/';
                            }
                            else {
                                this.loginFailed(res);
                            }
                        }}
                        onError={(err) => this.loginFailed(err)} />

                    <Dialog
                        open={this.state.dialogOpen}
                        onClose={() => this.dialogClose()}
                    >
                        <DialogTitle>{this.state.dialogTitle}</DialogTitle>
                        <DialogContent>
                            <Typography>{this.state.dialogMessage}</Typography>
                        </DialogContent>
                        <DialogActions>
                            <Button
                                color="primary"
                                onClick={() => this.dialogClose()}>Close</Button>
                        </DialogActions>
                    </Dialog>
                </DetailView>
            </div>
        );
    }
}

export const UserLoginForm: any = withStyles(styles)(UserLoginFormC);
