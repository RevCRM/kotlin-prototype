
import * as path from 'path';

import * as Koa from 'koa';
import * as mount from 'koa-mount';
import * as serve from 'koa-static';
import * as body from 'koa-bodyparser';
import * as session from 'koa-session';
import * as passport from 'koa-passport';

import { initialiseAuth, requireAuth } from './auth';
import { jsonLog } from 'koa-json-log';
import { registerRoutes } from './routes';

import { registerModels } from '../models/server';
import { populateData } from './data';
import { CRM_DIR, getRevCRMModulesInLoadOrder } from '../modules';
import { IModelManager, ModelManager, InMemoryBackend } from 'rev-models';
import { IModelApiManager, ModelApiManager } from 'rev-api';

const staticPath = path.join(CRM_DIR, 'dist', 'static');

export interface IRevCRMServerConfig {
    port?: number;
}

export class RevCRMServer {
    koa: Koa;
    models: IModelManager;
    api: IModelApiManager;

    constructor(public config: IRevCRMServerConfig = {}) {
        this.config.port = this.config.port || Number(process.env.NODE_PORT) || 3000;
        this.koa = new Koa();
        this.koa.keys = ['some_secret_here'];
        this.koa.use(jsonLog());
        this.koa.use(mount('/static', serve(staticPath)));
        this.koa.use(body()); // TODO Set options
        this.koa.use(session({ key: 'revcrm' }, this.koa));
        this.koa.use(passport.initialize());
        this.koa.use(passport.session());
        this.koa.use(requireAuth({
            unauthenticatedUrls: ['/login', '/static'],
            loginUrl: '/login'
        }));

        const backend = new InMemoryBackend();
        this.models = new ModelManager();
        this.models.registerBackend('default', backend);
        this.api = new ModelApiManager(this.models);

        registerRoutes(this);
    }

    private async loadModules() {
        const loadOrder = getRevCRMModulesInLoadOrder();
        for (const moduleName of loadOrder) {
            let mod: any = null;
            try {
                mod = require(path.join(CRM_DIR, 'node_modules', moduleName, 'lib', 'server'));
            }
            catch (e) {}
            if (mod) {
                console.log(`Loading ${moduleName}/lib/server ...`);
                await mod.register(this);
            }
        }
    }

    async start() {
        console.log('RevCRM Path:', CRM_DIR);
        registerModels(this);
        await populateData(this);
        initialiseAuth(this);
        await this.loadModules();
        this.koa.listen(this.config.port);
        console.log(`Server running on port ${this.config.port}`);
    }

}
