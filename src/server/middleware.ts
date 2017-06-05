import * as Hapi from 'hapi';
import * as Inert from 'inert';
import * as yar from 'yar';
import { api } from '../models/server';
import { ModelApiPlugin } from 'rev-api-hapi';

export function registerMiddleware(server: Hapi.Server) {
    return new Promise((resolve, reject) => {

        server.register<any>([
                Inert,
                {
                    register: ModelApiPlugin,
                    options: {
                        registry: api,
                        url: '/api',
                        graphiqlEnabled: true
                    }
                },
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
