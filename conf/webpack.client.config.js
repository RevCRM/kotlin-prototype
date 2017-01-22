var config = require('./webpack.base.config')();
var webpack = require('webpack');
var path = require('path');
var CopyWebpackPlugin = require('copy-webpack-plugin');

config.entry = './src/client/client.tsx';
config.target = 'web';
config.output.filename = 'static/client.js';
config.plugins.push(
    new webpack.DllReferencePlugin({
        manifest: require(path.join(config.output.path, 'static', 'clientlibs.json'))
    }),
    new CopyWebpackPlugin([
        { from: 'static', to: 'static', context: './src' }
    ])
)

module.exports = config;