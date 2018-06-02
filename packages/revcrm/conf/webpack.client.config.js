const webpack = require('webpack');
const path = require('path');
const fs = require('fs');

const CopyWebpackPlugin = require('copy-webpack-plugin');

const outputPath = path.resolve(process.cwd(), 'dist', 'static');
console.log('RevCRM Client Output Path:', outputPath);

function generateClientModuleImports() {
    const modules = require('../lib/modules');
    const clientModules = modules.getClientModuleList();

    const client_modules_ts = `// Auto-generated for webpack build
import { RevCRMClient } from 'revcrm/lib/client';
${clientModules.map((moduleName) => `import { register as register_${moduleName} } from '${moduleName}/lib/client';`).join('\n')}

export async function registerModules(client: RevCRMClient) {
${clientModules.map((moduleName) => `    await register_${moduleName}(client);`).join('\n')}
}
`;

    const clientModulesFile = path.resolve(process.cwd(), 'src', 'client_modules.ts');
    fs.writeFileSync(clientModulesFile, client_modules_ts);
}

module.exports = function() {

    generateClientModuleImports();

    return {
        entry: {
            client: './src/client.tsx'
        },
        output: {
            path: outputPath, 
            filename: 'client.js'
        },
        module: {
            rules: [
                { test: /\.tsx?$/, loader: 'ts-loader', exclude: /node_modules/ }
            ]
        },
        resolve: {
            extensions: ['.ts', '.tsx', '.js', '.jsx']
        },
        devtool: 'source-map',
        externals: [],
        plugins: [
            new webpack.DllReferencePlugin({
                manifest: require(path.join(outputPath, 'clientlibs.json'))
            }),
            new CopyWebpackPlugin([
                { from: 'static', context: './src' }
            ])
        ]
    }
}
