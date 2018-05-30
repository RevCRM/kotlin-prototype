const webpack = require('webpack');
const path = require('path');

const CopyWebpackPlugin = require('copy-webpack-plugin');

const outputPath = path.resolve(process.cwd(), 'dist', 'static');
console.log('RevCRM Client Output Path:', outputPath);

module.exports = function() {
    return {
        entry: './src/client/client.tsx',
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
