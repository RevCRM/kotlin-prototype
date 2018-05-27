var webpack = require('webpack');
var path = require('path');

var CopyWebpackPlugin = require('copy-webpack-plugin');

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
