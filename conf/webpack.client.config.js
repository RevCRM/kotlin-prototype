var webpack = require('webpack');
var path = require('path');

var CopyWebpackPlugin = require('copy-webpack-plugin');
var CheckerPlugin = require('awesome-typescript-loader').CheckerPlugin;

var outputPath = path.resolve(__dirname, '..', 'dist', 'static');

module.exports = function() {
    return {
        entry: './src/client/client.tsx',
        output: {
            path: outputPath, 
            filename: 'client.js'
        },
        module: {
            rules: [
                { enforce: 'pre', test: /\.tsx?$/, loader: 'tslint-loader', exclude: /(node_modules)/ },
                { test: /\.tsx?$/, loader: 'awesome-typescript-loader', exclude: /(node_modules)/ }
            ]
        },
        resolve: {
            extensions: ['.ts', '.tsx', '.js', '.jsx']
        },
        devtool: 'source-map',
        externals: [],
        plugins: [
            new CheckerPlugin(),
            new webpack.DllReferencePlugin({
                manifest: require(path.join(outputPath, 'clientlibs.json'))
            }),
            new CopyWebpackPlugin([
                { from: 'static', to: 'static', context: './src' },
                { from: 'node_modules/flexboxgrid/css/flexboxgrid.min.css', to: 'static'}
            ])
        ]
    }
}
