/*
 * Simple script to sync dependency source code from another local folder
 * for use during development. Avoids issues with webpack + npm link.
 * 
 * Requires the following folder structure:
 * 
 *   /some_folder/revcrm/revcrm  <- this project
 *   /some_folder/revjs/rev-models
 *   /some_folder/revjs/rev-forms
 *     ... etc.
 */

var path = require('path');
var fs = require('fs-extra');

var depPath = path.resolve(__dirname, '..', '..', '..', 'revjs');
var modulesPath = path.resolve(__dirname, '..', 'node_modules');

function syncDependency(depName) {
    console.log('Synching', depName);
    fs.removeSync(
        path.join(modulesPath, depName)
    );
    fs.copySync(
        path.join(depPath, depName, 'dist'),
        path.join(modulesPath, depName)
    );
    fs.copySync(
        path.join(depPath, depName, 'package.json'),
        path.join(modulesPath, depName, 'package.json')
    );
}

syncDependency('rev-models');
syncDependency('rev-forms');
syncDependency('rev-forms-redux-mui');
