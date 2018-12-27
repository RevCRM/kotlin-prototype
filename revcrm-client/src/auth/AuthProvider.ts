
import { User } from "./User"
import { EventEmitter } from "events"

export type AuthState = "none" | "initialising" | "logged_in" | "not_logged_in"

export interface IAuthProviderState {
    authState: AuthState
    currentUser: User
}

export enum AuthProviderEvents {
    authStateChanged = "authStateChanged"
}

export class AuthProvider extends EventEmitter {
    authState: AuthState
    currentUser: User | null

    // TODO: Support more auth providers
    _googleAuth!: gapi.auth2.GoogleAuth

    constructor() {
        super()
        this.authState = "none"
        this.currentUser = null
    }

    onUserLoggedIn(user: gapi.auth2.GoogleUser) {
        const profile = user.getBasicProfile()
        this.authState = "logged_in"
        this.currentUser = new User(
            profile.getEmail(),
            profile.getGivenName(),
            profile.getFamilyName()
        )
        this.emit(AuthProviderEvents.authStateChanged)
    }

    onUserLoggedOut() {
        this.authState = "not_logged_in"
        this.currentUser = null
        this.emit(AuthProviderEvents.authStateChanged)
    }

    async initialise() {
        this.authState = "initialising"
        this.emit(AuthProviderEvents.authStateChanged)

        await new Promise((resolve) => {
            gapi.load("auth2", resolve)
        })

        this._googleAuth = await new Promise((resolve, reject) => {
            gapi.auth2.init({
                client_id: "252486211013-8m24n1m58ugjjcn2qhm6mdgq1q4sganu.apps.googleusercontent.com",
                ux_mode: "redirect"
            })
            .then(resolve, reject)
        }) as any

        const user = this._googleAuth.currentUser.get()

        if (user && user.isSignedIn()) {
            this.onUserLoggedIn(user)
        }
        else {
            this.onUserLoggedOut()
        }
    }

    async logOut() {
        await this._googleAuth.signOut()
        this.onUserLoggedOut()
    }

    async logIn() {
        this._googleAuth.signIn()  // Triggers a browser redirect
    }

    async currentToken(): Promise<string> {
        const user = this._googleAuth.currentUser.get()
        const tokens = user.getAuthResponse()
        return tokens.id_token
    }

}
