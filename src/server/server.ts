
import * as Koa from 'koa';
import * as Router from 'koa-router';

import { logger } from './logging';
import { config } from './config';

/*
import { registerMiddleware } from './middleware';
import { registerRoutes } from './routes';
import { serverModels } from '../models/server';
import { populateTestData } from '../models/test_data';
*/

const app = new Koa();

app.use(logger);

const router = new Router();
router.get('/*', async (ctx) => {
    ctx.body = 'Hello World!';
});
app.use(router.routes());

app.listen(config.port);

console.log(`Server running on port ${config.port}`);

/*
routes: {
        files: {
            relativeTo: path.resolve(__dirname, '..', 'static')
        }
    }
*/
