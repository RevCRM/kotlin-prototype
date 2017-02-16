var config = require('./webpack.base.config')();
var nodeExternals = require('webpack-node-externals');
var webpack = require('webpack');

config.entry = './src/server/server.ts';
config.target = 'node';
config.node = {
    __dirname: false,
    __filename: false
}
config.output.filename = 'server/server.js';
config.externals.push(nodeExternals());
config.plugins.push(
    new webpack.BannerPlugin({
        banner: 'require("source-map-support").install();',
        raw: true, entryOnly: true})
);

module.exports = config;