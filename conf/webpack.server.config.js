var config = require('./webpack.base.config')();
var nodeExternals = require('webpack-node-externals');

config.entry = './src/server/server.ts';
config.target = 'node';
config.node = {
    __dirname: false,
    __filename: false
}
config.output.filename = 'server/server.js';
config.externals.push(nodeExternals());

module.exports = config;