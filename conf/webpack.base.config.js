var path = require('path');

var CheckerPlugin = require('awesome-typescript-loader').CheckerPlugin;

module.exports = function() {
    return {
        entry: '',
        target: '',
        output: {
            path: path.resolve(__dirname, '..', 'dist'), 
            filename: 'server.js',
            publicPath: '/static/',
        },
        externals: [],
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
            new CheckerPlugin()
        ]
    }
}