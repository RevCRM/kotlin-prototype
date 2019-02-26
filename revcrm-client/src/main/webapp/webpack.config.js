const path = require('path');
const webpack = require('webpack');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
const CopyWebpackPlugin = require('copy-webpack-plugin');

const outputPath = path.resolve(__dirname, '..', 'resources', 'public');

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
            filename: "static/js/[name].js",
            chunkFilename: 'static/js/[name].js'
        },
        resolve: {
            // Add `.ts` and `.tsx` as a resolvable extension.
            extensions: [".ts", ".tsx", ".js"],
            alias: {
                "@material-ui/core": path.resolve(__dirname, "node_modules/@material-ui/core/es"),
                "britecharts-react": path.resolve(__dirname, "node_modules/britecharts-react/lib/esm")
            }
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
            new CopyWebpackPlugin([
                { from: 'index.html' },
                { from: 'static/**/*' },
                { from: 'node_modules/britecharts-react/dist/britecharts-react.min.css', to: 'static/css/britecharts-react.min.css' }
            ])
        ]
    };

    if (args.analyzeBundle) {
        config.plugins.push(new BundleAnalyzerPlugin())
    }

    if (mode == 'development') {
        config.devtool = "source-map"
    }

    return config;
};
