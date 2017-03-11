import * as Hapi from 'hapi';
import * as Inert from 'inert';
import * as yar from 'yar';
import '../models/api';
import { RevApiPlugin } from 'rev-api-hapi';

export default function registerMiddleware(server: Hapi.Server) {
    return new Promise((resolve, reject) => {

        server.register([
                Inert,
                RevApiPlugin,
                {
                    register: yar,
                    options: {
                        storeBlank: false,
                        maxCookieSize: 0,
                        cookieOptions: {
                            // TODO: This should be set by users in a config file
                            password: 'XbraPDzS*AH%c2Thz4x!!PSX?EWUc-@w',
                            isSecure: false,
                            isHttpOnly: true
                        }
                    }
                }
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
