
export class GoogleSignIn {
    _auth!: gapi.auth2.GoogleAuth;
    currentUser: gapi.auth2.GoogleUser | null = null;

    async initialise() {

        await new Promise((resolve) => {
            gapi.load('auth2', resolve);
        });

        this._auth = await new Promise((resolve, reject) => {
            gapi.auth2.init({
                client_id: '252486211013-8m24n1m58ugjjcn2qhm6mdgq1q4sganu.apps.googleusercontent.com',
                ux_mode: 'redirect'
            })
            .then(resolve, reject);
        }) as any;

        this.currentUser = this._auth.currentUser.get();

    }

    async logout() {
        await this._auth.signOut();
        this.currentUser = null;
    }

    async login() {
        const loggedInUser = await this._auth.signIn();
        this.currentUser = loggedInUser;
    }
}
