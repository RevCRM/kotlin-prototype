var config = require('./webpack.base.config')();
var webpack = require('webpack');
var path = require('path');

config.entry = './src/client/client.tsx';
config.target = 'web';
config.output.filename = 'static/client.js';
config.plugins.push(
    new webpack.DllReferencePlugin({
        manifest: require(path.join(config.output.path, 'static', 'clientlibs.json'))
    })
)

module.exports = config;