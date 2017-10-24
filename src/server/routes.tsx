import * as Router from 'koa-router';

import { graphqlKoa, graphiqlKoa } from 'graphql-server-koa';
import { api } from '../models/server';

import * as React from 'react';
import { BasePage } from './BasePage';
import { renderToStaticMarkup } from 'react-dom/server';

const router = new Router();

router.get('/static/*', async (ctx) => {
    ctx.status = 404;
});

const schema = api.getGraphQLSchema();
router.post('/api', graphqlKoa({ schema: schema }));
router.get('/api', graphqlKoa({ schema: schema }));
router.get('/graphiql', graphiqlKoa({ endpointURL: '/api' }));

router.get('/*', async (ctx) => {

    const clientScripts = [
        '/static/clientlibs.js',
        '/static/client.js'
    ];

    const clientCss = [
        'https://fonts.googleapis.com/css?family=Roboto:300,400,500',
        '/static/flexboxgrid.min.css',
        '/static/style.css'
    ];

    ctx.body = renderToStaticMarkup(
        <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
    );

});

export { router };
