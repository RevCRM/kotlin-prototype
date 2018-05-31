
import * as path from 'path';
import * as fs from 'fs';

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

const CRM_DIR = process.cwd();
const staticPath = path.join(CRM_DIR, 'dist', 'static');

export interface IRevCRMServerConfig {
    port?: number;
}

export interface IModulesAndDependencies {
    [moduleName: string]: string[];
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

    getRevCRMModules(): IModulesAndDependencies {
        const modulesFolder = path.join(CRM_DIR, 'node_modules');

        // Get list of node modules
        const modules = fs.readdirSync(modulesFolder)
            .filter((item) => {
                const itemPath = path.join(modulesFolder, item, 'package.json');
                try {
                    return fs.lstatSync(itemPath).isFile();
                }
                catch (e) {
                    return false;
                }
            });

        // Get list of CRM modules and their dependencies
        const crmModules: { [moduleName: string]: string[] } = {};
        modules.forEach((moduleName) => {
            const pkgJson = require(path.join(modulesFolder, moduleName, 'package.json'));
            if (pkgJson.keywords && pkgJson.keywords.includes('revcrm_module')) {
                crmModules[moduleName] = Object.keys(pkgJson.dependencies);
            }
        });

        // Strip non-CRM dependencies
        const crmModuleNames = Object.keys(crmModules);
        crmModuleNames.forEach((moduleName) => {
            crmModules[moduleName] = crmModules[moduleName]
                .filter((dependency) => crmModuleNames.includes(dependency));
        });

        return crmModules;
    }

    getRevCRMModuleLoadOrder(): string[] {
        const modules = this.getRevCRMModules();
        const result: string[] = [];
        for (const moduleName in modules) {
            resolveDeps(modules, result, moduleName, [moduleName]);
        }
        return result;
    }

    async start() {
        console.log('RevCRM Path:', CRM_DIR);
        const loadOrder = this.getRevCRMModuleLoadOrder();
        console.log('RevCRM Modules:', loadOrder);
        await populateData(serverModels);
        this._koa.listen(this.config.port);
        console.log(`Server running on port ${this.config.port}`);
    }

}

function resolveDeps(depTree: IModulesAndDependencies, result: string[], dependant: string, depPath: string[]) {
    if (depPath.indexOf(dependant) !== depPath.lastIndexOf(dependant)) {
        throw new Error('circular dependency found: ' + depPath.join(' > '));
    }

    if (depTree[dependant]) {
        depTree[dependant].forEach((depender) => {
            resolveDeps(depTree, result, depender, depPath.concat(depender));
        });
    }

    if (!result.includes(dependant)) {
        result.push(dependant);
        delete depTree[dependant];
    }
}
