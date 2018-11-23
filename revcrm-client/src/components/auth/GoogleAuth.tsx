
import * as React from 'react';
import { GoogleSignIn } from '../../auth/GoogleSignIn';

export interface IGoogleAuthState {
    loadState: 'loading' | 'loaded' | 'load_error';
    authState: 'logged_in' | 'logged_out' | 'logging_in';
    currentUser: string;
}

export class GoogleAuth extends React.Component<{}, IGoogleAuthState> {
    _googleAuth = new GoogleSignIn();

    constructor(props: any) {
        super(props);
        this.state = {
            loadState: 'loading',
            authState: 'logged_out',
            currentUser: ''
        };
        this.loadData();
    }

    async loadData() {
        try {

            await this._googleAuth.initialise();
            this.setState({ loadState: 'loaded' });

            if (this._googleAuth.currentUser && this._googleAuth.currentUser.isSignedIn()) {
                const profile = this._googleAuth.currentUser.getBasicProfile();
                this.setState({
                    authState: 'logged_in',
                    currentUser: profile.getEmail()
                });
            }
            else {
                this.setState({
                    authState: 'logged_out',
                    currentUser: ''
                });
            }
        }
        catch (e) {
            console.error(e);
            this.setState({ loadState: 'load_error' });
        }
    }

    logout = async () => {
        await this._googleAuth.logout();
        this.setState({
            authState: 'logged_out'
        });
    }

    login = async () => {
        await this._googleAuth.login();
        if (this._googleAuth.currentUser) {
            const profile = this._googleAuth.currentUser.getBasicProfile();
            this.setState({
                authState: 'logged_in',
                currentUser: profile.getEmail()
            });
        }
    }

    testAPI = async () => {
        console.log('Sending test request...');
        const tokens = this._googleAuth.currentUser!.getAuthResponse();
        const idToken = tokens.id_token;
        console.log('TOKEN', idToken);
        const res = fetch('/ping', {
            headers: {
                Authorization: 'Bearer ' + idToken
            }
        });
    }

    render() {
        if (this.state.loadState == 'loading') {
            return 'Initialising Google Auth...';
        }
        else if (this.state.loadState == 'load_error') {
            return 'Error initialising Google Auth :(';
        }
        else {

            if (this.state.authState == 'logged_in') {
                return (<>
                    <p>Logged In as {this.state.currentUser}</p>
                    <button onClick={this.logout}>Log Out</button>
                    <button onClick={this.testAPI}>Test API</button>
                </>);
            }
            else if (this.state.authState == 'logging_in') {
                return (
                    <p>Logging In...</p>
                );
            }
            else {
                return (<>
                    <p>Logged Out</p>
                    <button onClick={this.login}>Log In</button>
                </>);
            }

        }
    }

}
