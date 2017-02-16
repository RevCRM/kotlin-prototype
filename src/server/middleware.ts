import * as Hapi from 'hapi';
import * as Inert from 'inert';
import RevApi from 'rev-api-hapi';

export default function registerMiddleware(server: Hapi.Server) {
    return new Promise((resolve, reject) => {

        server.register([
                Inert,
                RevApi
            ], (err) => {
            if (err) {
                reject(err);
            }
            else {
                resolve();
            }
        });

    });
}
