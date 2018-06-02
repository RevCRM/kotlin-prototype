
import * as path from 'path';
import * as fs from 'fs';

export interface IModuleMeta {
    [moduleName: string]: {
        name: string;
        server: boolean;
        client: boolean;
        dependencies: string[];
    };
}

export const CRM_DIR = process.cwd();

export function getCRMModuleMeta(): IModuleMeta {
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

    // Get list of CRM modules, their dependencies and whether they are client-side and/or server side
    const crmModules: IModuleMeta = {};
    modules.forEach((moduleName) => {
        const pkgJsonPath = path.join(modulesFolder, moduleName, 'package.json');
        const pkgJson = require(pkgJsonPath);
        if (pkgJson.keywords && pkgJson.keywords.includes('revcrm_module')) {
            const serverPath = path.join(modulesFolder, moduleName, 'lib', 'server.js');
            const clientPath = path.join(modulesFolder, moduleName, 'lib', 'client.js');
            let server = false;
            let client = false;
            try { server = fs.lstatSync(serverPath).isFile(); } catch (e) {}
            try { client = fs.lstatSync(clientPath).isFile(); } catch (e) {}
            crmModules[moduleName] = {
                name: pkgJson.name,
                client, server,
                dependencies: Object.keys(pkgJson.dependencies)
            };
        }
    });

    // Strip non-CRM dependencies
    const crmModuleNames = Object.keys(crmModules);
    crmModuleNames.forEach((moduleName) => {
        crmModules[moduleName].dependencies = crmModules[moduleName].dependencies
            .filter((dependency) => crmModuleNames.includes(dependency));
    });

    return crmModules;
}

export interface IModulesAndDependencies {
    [moduleName: string]: string[];
}

export function getCRMModulesInLoadOrder(modules: IModuleMeta): string[] {
    const depTree: IModulesAndDependencies = {};
    for (const moduleName in modules) {
        depTree[moduleName] = modules[moduleName].dependencies.slice();
    }
    const result: string[] = [];
    for (const moduleName in modules) {
        dependencyOrder(depTree, result, moduleName, [moduleName]);
    }
    return result;
}

function dependencyOrder(depTree: IModulesAndDependencies, result: string[], dependant: string, depPath: string[]) {
    if (depPath.indexOf(dependant) !== depPath.lastIndexOf(dependant)) {
        throw new Error('circular dependency found: ' + depPath.join(' > '));
    }

    if (depTree[dependant]) {
        depTree[dependant].forEach((depender) => {
            dependencyOrder(depTree, result, depender, depPath.concat(depender));
        });
    }

    if (!result.includes(dependant)) {
        result.push(dependant);
        delete depTree[dependant];
    }
}

export function getClientModuleList(): string[] {
    const meta = getCRMModuleMeta();
    const loadOrder = getCRMModulesInLoadOrder(meta);
    const clientModuleList = [];
    for (const moduleName of loadOrder) {
        const modMeta = meta[moduleName];
        if (modMeta.client) {
            clientModuleList.push(moduleName);
        }
    }
    return clientModuleList;
}
