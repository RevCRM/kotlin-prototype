import * as Router from 'koa-router';
import * as passport from 'koa-passport';

import { graphqlKoa, graphiqlKoa } from 'apollo-server-koa';
import { api } from '../models/server';

import * as React from 'react';
import { BasePage } from './BasePage';
import { renderToStaticMarkup } from 'react-dom/server';
import { UserAuthData } from '../models/User/backend/UserAuthData';

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
    'https://fonts.googleapis.com/css?family=Roboto:300,400,500'
];

router.get('/login', async (ctx) => {
    ctx.body = renderToStaticMarkup(
        <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
    );
});

router.post('/login', async (ctx, next) => {
    return passport.authenticate('local', (err: any, user: UserAuthData) => {
        if (user && user.id) {
            ctx.body = '';
            return ctx.login(user);
        }
        else if (err) {
            console.log('Error occured validating login', err);
        }
        ctx.throw(401, '');
    })(ctx, next);
});

router.get('/*', async (ctx) => {
    ctx.body = renderToStaticMarkup(
        <BasePage title="RevCRM" scripts={clientScripts} css={clientCss} />
    );
});

export { router };
