import * as React from 'react';
import * as Hapi from 'hapi';
import BasePage from './BasePage';
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

export default function registerRoutes(server: Hapi.Server) {
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
            handler: (request: Hapi.Request, reply: Hapi.IReply) => {
                reply(renderToStaticMarkup(
                    <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
                ));
            }
        });

        resolve();
    });
}
