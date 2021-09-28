'use strict';

const {join} = require('path');
const port = process.env.port || process.env.npm_config_port || 3001;

console.log(port);

module.exports = {
    pages: {
        index: 'src/main.js',
    },
    outputDir: join(__dirname, 'dist'),
    filenameHashing: false,
    productionSourceMap: false,
    publicPath: './',
    devServer: {
        port,
        disableHostCheck: true,
        contentBase: [join(__dirname, 'public')],
        proxy: {
            '^/gateway': {
                target: 'http://localhost:9002',
                changeOrigin: true,
                pathRewrite: {'^/gateway': ''},
                logLevel: 'debug'
            }
        }
    },
    configureWebpack: {
        // provide the app's title in webpack's name field, so that
        // it can be accessed in index.html to inject the correct title.
        name: 'citrus',
        resolve: {
            alias: {
                '@': join(__dirname, 'src'),
            },
        }

    },
};
