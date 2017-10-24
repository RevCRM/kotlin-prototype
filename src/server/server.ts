
import * as path from 'path';

import * as Koa from 'koa';
import * as mount from 'koa-mount';
import * as serve from 'koa-static';
import * as body from 'koa-bodyparser';

import { logger } from './logging';
import { config } from './config';
import { router } from './routes';

// import { serverModels } from '../models/server';
// import { populateTestData } from '../models/test_data';

const staticPath = path.join(__dirname, '..', '..', 'dist', 'static');

const app = new Koa();

app.use(logger);
app.use(mount('/static', serve(staticPath, { defer: false })));
app.use(body()); // TODO Set options
app.use(router.routes());
app.use(router.allowedMethods());

app.listen(config.port);

console.log(`Server running on port ${config.port}`);
