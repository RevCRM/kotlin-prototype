
import * as path from 'path';

import * as Koa from 'koa';
import * as mount from 'koa-mount';
import * as serve from 'koa-static';
import * as body from 'koa-bodyparser';
import * as session from 'koa-session';
import * as passport from 'koa-passport';

import './auth';
import { logger } from './logging';
import { config } from './config';
import { router } from './routes';

import { serverModels } from '../models/server';
import { populateTestData } from '../models/test_data';

const staticPath = path.join(__dirname, '..', '..', 'dist', 'static');

const app = new Koa();
app.keys = ['some_secret_here'];

populateTestData(serverModels)
.then(() => {

    app.use(logger);
    app.use(mount('/static', serve(staticPath)));
    app.use(body()); // TODO Set options
    app.use(session({ key: 'revcrm' }, app));
    app.use(passport.initialize());
    app.use(passport.session());
    app.use(router.routes());
    app.use(router.allowedMethods());

    app.listen(config.port);

    console.log(`Server running on port ${config.port}`);

})
.catch((e) => {
    console.log(e);
    console.log(e.result.validation);
});
