import * as Hapi from 'hapi';
import * as Inert from 'inert';

export default function registerMiddleware(server: Hapi.Server) {
    return new Promise((resolve, reject) => {

        server.register(Inert, (err) => {
            if (err) {
                reject(err);
            }
            else {
                resolve();
            }
        });

    });
}
