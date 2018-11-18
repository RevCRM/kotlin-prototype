
import * as React from 'react';
import { GoogleSignIn } from '../../auth/GoogleSignIn';

export interface IGoogleAuthState {
    loadState: 'loading' | 'loaded' | 'load_error';
    authState: 'logged_in' | 'logged_out' | 'logging_in';
}

export class GoogleAuth extends React.Component<{}, IGoogleAuthState> {
    _googleAuth = new GoogleSignIn();

    constructor(props: any) {
        super(props);
        this.state = {
            loadState: 'loading',
            authState: 'logged_out',
        };
        this.loadData();
    }

    async loadData() {
        try {
            await this._googleAuth.initialise();
            if (this._googleAuth.auth.isSignedIn.get()) {
                this.setState({
                    loadState: 'loaded',
                    authState: 'logged_in'
                });
            }
            else {
                this.setState({
                    loadState: 'loaded',
                    authState: 'logged_out'
                });
            }
        }
        catch (e) {
            console.error(e);
            this.setState({ loadState: 'load_error' });
        }
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
                    <p>Logged In</p>
                    <button>Log Out</button>
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
                    <button>Log In</button>
                </>);
            }

        }
    }

}
