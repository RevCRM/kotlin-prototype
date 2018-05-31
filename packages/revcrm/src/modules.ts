
import * as path from 'path';
import * as fs from 'fs';

export interface IModulesAndDependencies {
    [moduleName: string]: string[];
}

export function getRevCRMModules(crmPath: string): IModulesAndDependencies {
    const modulesFolder = path.join(crmPath, 'node_modules');

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

export function getRevCRMModuleLoadOrder(crmPath: string): string[] {
    const modules = getRevCRMModules(crmPath);
    const result: string[] = [];
    for (const moduleName in modules) {
        dependencyOrder(modules, result, moduleName, [moduleName]);
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
