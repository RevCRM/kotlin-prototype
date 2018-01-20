
import * as React from 'react';
import * as rev from 'rev-models';
import { FormView, Field, ViewAction } from 'rev-forms-materialui';
import Dialog, { DialogTitle, DialogContent, DialogActions} from 'material-ui/Dialog';
import Grid from 'material-ui/Grid';
import Button from 'material-ui/Button';
import { withStyles, WithStyles } from 'material-ui/styles';
import Typography from 'material-ui/Typography';

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

    loginFailed() {
        this.setState({
            dialogOpen: true,
            dialogTitle: 'Login Failed',
            dialogMessage: 'Please check your entries and try again.'
        });
    }

    render() {
        return (
            <div className={this.props.classes.root}>
                <FormView model="UserLoginFormModel">
                    <Grid container spacing={8}>
                        <Field name="username" colspan={12} />
                        <Field name="password" colspan={12} />
                    </Grid>

                    <ViewAction
                        label="Log In"
                        type="post"
                        url="/login"
                        default={true}
                        onSuccess={() => window.location.pathname = '/'}
                        onFailure={() => this.loginFailed()} />

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
                </FormView>
            </div>
        );
    }
}

export const UserLoginForm = withStyles(styles)(UserLoginFormC);
