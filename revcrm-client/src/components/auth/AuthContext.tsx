
import * as React from "react"
import { User } from "../../auth/User"
import { Omit } from "../../types"
import { History } from "history"
import { getViewStateFromUrl } from "../views/ViewManager"
import { AuthProvider, AuthProviderEvents, AuthState } from "../../auth/AuthProvider"

export interface IAuthProviderProps {
    provider: AuthProvider
    history: History<any>
}

export interface IAuthProviderState {
    authState: AuthState
}

export interface IAuthContext {
    authState: AuthState
    currentUser: User | null
    login(): void
    logout(): void
}

export const AuthContext = React.createContext<IAuthContext>(null as any)

// TODO: should be in user profile
export const HOME_URL = "/dashboard/my"

export class AuthContextProvider extends React.Component<IAuthProviderProps, IAuthProviderState> {

    constructor(props: any) {
        super(props)
        this.state = {
            authState: "initialising",
        }
    }

    componentDidMount() {
        const { provider } = this.props
        provider.addListener(
            AuthProviderEvents.authStateChanged,
            this.onAuthStateChanged)
        if (provider.authState == "none") {
            provider.initialise()
        }
    }

    componentWillUnmount() {
        this.props.provider.removeListener(
            AuthProviderEvents.authStateChanged,
            this.onAuthStateChanged)
    }

    onAuthStateChanged = () => {
        const { authState } = this.props.provider
        console.log("new auth state", authState)
        if (authState == "logged_in" && this.state.authState == "initialising") {
            this.setInitialUrl()
        }
        this.setState({
            authState
        })
    }

    setInitialUrl() {
        const view = getViewStateFromUrl(
            this.props.history.location.pathname,
            this.props.history.location.search
        )
        if (!view.view) {
            this.props.history.push(HOME_URL)
        }
    }

    logout = async () => {
        await this.props.provider.logOut()
    }

    login = async () => {
        await this.props.provider.logIn()
    }

    render() {
        const authContext: IAuthContext = {
            authState: this.state.authState,
            currentUser: this.props.provider.currentUser,
            login: this.login,
            logout: this.logout
        }
        return (
            <AuthContext.Provider value={authContext}>
                {this.props.children}
            </AuthContext.Provider>
        )
    }

}

export interface IAuthContextProp {
    auth: IAuthContext
}

export function withAuthContext<
    TComponentProps extends IAuthContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IAuthContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: any): React.ReactElement<TComponentProps> => (
        <AuthContext.Consumer>{(auth) => (
            <Component auth={auth} {...props} />
        )}</AuthContext.Consumer>
    )
}
