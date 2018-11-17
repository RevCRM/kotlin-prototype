
export class GoogleSignIn {

    async initialise() {

        await new Promise((resolve) => {
            gapi.load('auth2', resolve);
        });

        const auth: gapi.auth2.GoogleAuth = await new Promise((resolve, reject) => {
            gapi.auth2.init({
                client_id: '252486211013-8m24n1m58ugjjcn2qhm6mdgq1q4sganu.apps.googleusercontent.com',
            })
            .then(resolve, reject);
        }) as any;

        if (auth.isSignedIn.get()) {
            console.log('user signed in');
        }
        else {
            console.log('user not signed in');
        }
    }
}
