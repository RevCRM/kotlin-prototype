
var webpack = require('webpack');
var path = require('path');

var outputPath = path.resolve(__dirname, '..', 'dist', 'static');

module.exports = {
    entry: {
        clientlibs: [
            'react', 
            'react-dom', 
            'react-router-dom',
            'redux',
            'react-redux',
            'material-ui'
        ]
    },
    output: { 
        filename: '[name].js',
        path: outputPath,
        library: '[name]', 
    },
    performance: {
        hints: false
    },
    plugins: [ 
        new webpack.DllPlugin({ 
            name: '[name]', 
            path: path.join(outputPath, '[name].json') 
        }),
        new webpack.DefinePlugin({
            'process.env': {
                NODE_ENV: JSON.stringify('production')
            }
        })
    ]
}