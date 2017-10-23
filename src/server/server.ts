
import * as path from 'path';

import * as Koa from 'koa';
import * as mount from 'koa-mount';
import * as serve from 'koa-static';

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

const staticPath = path.join(__dirname, '..', '..', 'dist', 'static');
app.use(mount('/static', serve(staticPath)));

app.listen(config.port);

console.log(`Server running on port ${config.port}`);

/*
routes: {
        files: {
            relativeTo: path.resolve(__dirname, '..', 'static')
        }
    }
*/
