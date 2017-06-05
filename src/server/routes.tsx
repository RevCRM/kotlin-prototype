import * as React from 'react';
import * as Hapi from 'hapi';
import { BasePage } from './BasePage';
import { renderToStaticMarkup } from 'react-dom/server';

const clientScripts = [
    '/static/clientlibs.js',
    '/static/client.js'
];

const clientCss = [
    'https://fonts.googleapis.com/css?family=Roboto:300,400,500',
    '/static/flexboxgrid.min.css',
    '/static/style.css'
];

export function registerRoutes(server: Hapi.Server) {
    return new Promise((resolve, reject) => {

        server.route({
            method: 'GET',
            path: '/static/{param*}',
            handler: {
                directory: {
                    path: '.'
                }
            }
        });

        server.route({
            method: 'GET',
            path: '/{param*}',
            handler: (request: Hapi.Request, reply: Hapi.ReplyNoContinue) => {

                const session = (request as any).yar;
                if (!session.get('userkey')) {
                    session.set('userkey', new Date().getTime());
                }
                console.log(session.get('userkey'));

                reply(renderToStaticMarkup(
                    <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
                ));
            }
        });

        resolve();
    });
}
