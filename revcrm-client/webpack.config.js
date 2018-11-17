const path = require('path');
const webpack = require('webpack');
const CopyWebpackPlugin = require('copy-webpack-plugin');

const outputPath = path.join(__dirname, 'dist');

module.exports = (env, args) => {

    const mode = args.mode || 'development';
    console.log('Webpack mode:', mode);

    const config = {
        mode: mode,
        entry: {
            client: "./src/client.tsx",
        },
        output: {
            path: outputPath,
            filename: "[name].js",
            chunkFilename: '[name].js'
        },
        resolve: {
            // Add `.ts` and `.tsx` as a resolvable extension.
            extensions: [".ts", ".tsx", ".js"],
        },
        module: {
            rules: [
                {
                    test: /\.tsx?$/, enforce: 'pre',
                    use: [{
                        loader: 'tslint-loader',
                        options: {
                            failOnHint: true
                        }
                    }]
                },
                { test: /\.tsx?$/, loader: "ts-loader" }
            ]
        },
        plugins: [
            new webpack.DefinePlugin({
                'process.env': {
                    NODE_ENV: JSON.stringify(mode)
                },
            }),
            // new CopyWebpackPlugin([
            //     { context: 'static', from: '**/*' }
            // ])
        ]
    };

    return config;
};
