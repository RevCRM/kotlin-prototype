import * as Router from 'koa-router';
import * as passport from 'koa-passport';

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

const clientScripts = [
    '/static/clientlibs.js',
    '/static/client.js'
];

const clientCss = [
    'https://fonts.googleapis.com/css?family=Roboto:300,400,500',
    '/static/flexboxgrid.min.css',
    '/static/style.css'
];

router.get('/', async (ctx) => {
    ctx.body = 'are ya logged in?... ' + (ctx.isAuthenticated() ? 'Yup!' : 'Nope!');
});

router.post('/login', passport.authenticate('local', {
    successRedirect: '/',
    failureRedirect: '/'
}));

router.get('/crm/*', async (ctx) => {
    ctx.body = renderToStaticMarkup(
        <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
    );
});

export { router };
