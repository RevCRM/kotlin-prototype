
import * as React from 'react';
import { User } from './User';
import { Omit } from '../../types';
import { History } from 'history';
import { getViewStateFromUrl } from '../views/ViewManager';

export type AuthState = 'initialising' | 'logged_in' | 'not_logged_in';

export interface IAuthProviderProps {
    history: History<any>;
}

export interface IAuthProviderState {
    authState: AuthState;
    currentUser: User;
}

export interface IAuthContext {
    authState: AuthState;
    currentUser: User;
    login(): void;
    logout(): void;
}

export const AuthContext = React.createContext<IAuthContext>(null as any);

// TODO: should be in user profile
export const HOME_URL = '/dashboard/my';

export class AuthContextProvider extends React.Component<IAuthProviderProps, IAuthProviderState> {
    // TODO: Abstract auth provider
    _googleAuth!: gapi.auth2.GoogleAuth;

    constructor(props: any) {
        super(props);
        this.state = {
            authState: 'initialising',
            currentUser: null as any
        };
        this.initialise();
    }

    setInitialUrl(user: gapi.auth2.GoogleUser) {
        const view = getViewStateFromUrl(
            this.props.history.location.pathname,
            this.props.history.location.search
        );
        if (!view.view) {
            this.props.history.push(HOME_URL);
        }
    }

    setLoggedInUser(user: gapi.auth2.GoogleUser) {
        const profile = user.getBasicProfile();
        this.setState({
            authState: 'logged_in',
            currentUser: new User(
                profile.getEmail(),
                profile.getGivenName(),
                profile.getFamilyName()
            )
        });
    }

    async initialise() {
        await new Promise((resolve) => {
            gapi.load('auth2', resolve);
        });

        this._googleAuth = await new Promise((resolve, reject) => {
            gapi.auth2.init({
                client_id: '252486211013-8m24n1m58ugjjcn2qhm6mdgq1q4sganu.apps.googleusercontent.com',
                ux_mode: 'redirect'
            })
            .then(resolve, reject);
        }) as any;

        const user = this._googleAuth.currentUser.get();

        if (user && user.isSignedIn()) {
            this.setInitialUrl(user);
            this.setLoggedInUser(user);
        }
        else {
            this.setState({
                authState: 'not_logged_in',
                currentUser: null as any
            });
        }
    }

    logout = async () => {
        await this._googleAuth.signOut();
        this.setState({
            authState: 'not_logged_in',
            currentUser: null as any
        });
    }

    login = async () => {
        this._googleAuth.signIn();
    }

    // TODO: Remove me
    testAPI = async () => {
        const user = this._googleAuth.currentUser.get();
        const tokens = user.getAuthResponse();
        const idToken = tokens.id_token;
        console.log('Sending test request...');
        console.log('TOKEN', idToken);
        const res = fetch('/ping', {
            headers: {
                Authorization: 'Bearer ' + idToken
            }
        });
        console.log(res);
    }

    render() {
        const authContext: IAuthContext = {
            authState: this.state.authState,
            currentUser: this.state.currentUser,
            login: this.login,
            logout: this.logout
        };
        return (
            <AuthContext.Provider value={authContext}>
                {this.props.children}
            </AuthContext.Provider>
        );
    }

}

export interface IAuthContextProp {
    auth: IAuthContext;
}

export function withAuthContext<
    TComponentProps extends IAuthContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IAuthContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: TWrapperProps): React.ReactElement<TComponentProps> => (
        <AuthContext.Consumer>{(auth) => (
            <Component auth={auth} {...props} />
        )}</AuthContext.Consumer>
    );
}
