
export class GoogleSignIn {
    auth!: gapi.auth2.GoogleAuth;

    async initialise() {

        await new Promise((resolve) => {
            gapi.load('auth2', resolve);
        });

        this.auth = await new Promise((resolve, reject) => {
            gapi.auth2.init({
                client_id: '252486211013-8m24n1m58ugjjcn2qhm6mdgq1q4sganu.apps.googleusercontent.com',
            })
            .then(resolve, reject);
        }) as any;

    }
}
