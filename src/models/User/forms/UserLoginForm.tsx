
import * as React from 'react';
import * as rev from 'rev-models';
import { ModelForm, ModelField, FormAction } from 'rev-forms-materialui';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';

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

export class UserLoginForm extends React.Component<null, IUserLoginFormState> {

    constructor() {
        super();
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
            <ModelForm model="UserLoginFormModel">
                <ModelField name="username" />
                <ModelField name="password" />

                <FormAction
                    label="Log In"
                    type="post"
                    url="/login"
                    default={true}
                    onSuccess={() => window.location.pathname = '/'}
                    onFailure={() => this.loginFailed()} />

                <Dialog
                    title={this.state.dialogTitle}
                    actions={[
                        <FlatButton
                            label="Close"
                            primary={true}
                            onClick={() => this.dialogClose()}
                        />
                    ]}
                    modal={false}
                    open={this.state.dialogOpen}
                    onRequestClose={() => this.dialogClose()}>
                    {this.state.dialogMessage}
                </Dialog>
            </ModelForm>
        );
    }
}
