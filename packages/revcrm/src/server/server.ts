
import * as path from 'path';

import * as Koa from 'koa';
import * as mount from 'koa-mount';
import * as serve from 'koa-static';
import * as body from 'koa-bodyparser';
import * as session from 'koa-session';
import * as passport from 'koa-passport';

import { requireAuth } from './auth';
import { jsonLog } from 'koa-json-log';
import { router } from './routes';

import { serverModels } from '../models/server';
import { populateData } from './data';
import { IModulesAndDependencies, getRevCRMModuleLoadOrder } from '../modules';

const CRM_DIR = process.cwd();
const staticPath = path.join(CRM_DIR, 'dist', 'static');

export interface IRevCRMServerConfig {
    port?: number;
}

export class RevCRMServer {
    _koa: Koa;

    constructor(public config: IRevCRMServerConfig = {}) {
        this.config.port = this.config.port || Number(process.env.NODE_PORT) || 3000;
        this._koa = new Koa();
        this._koa.keys = ['some_secret_here'];
        this._koa.use(jsonLog());
        this._koa.use(mount('/static', serve(staticPath)));
        this._koa.use(body()); // TODO Set options
        this._koa.use(session({ key: 'revcrm' }, this._koa));
        this._koa.use(passport.initialize());
        this._koa.use(passport.session());
        this._koa.use(requireAuth({
            unauthenticatedUrls: ['/login', '/static'],
            loginUrl: '/login'
        }));
        this._koa.use(router.routes());
        this._koa.use(router.allowedMethods());
    }

    async start() {
        console.log('RevCRM Path:', CRM_DIR);
        const loadOrder = getRevCRMModuleLoadOrder(CRM_DIR);
        console.log('RevCRM Modules:', loadOrder);
        await populateData(serverModels);
        this._koa.listen(this.config.port);
        console.log(`Server running on port ${this.config.port}`);
    }

}
