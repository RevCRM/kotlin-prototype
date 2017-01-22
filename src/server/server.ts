
import * as Hapi from 'hapi';
import * as path from 'path';

import registerMiddleware from './middleware';
import registerRoutes from './routes';
import '../components/models';

const server = new Hapi.Server();

server.connection({
    port: process.env.REVCRM_PORT || 8800,
    routes: {
        files: {
            relativeTo: path.resolve(__dirname, '..', 'static')
        }
    }
});

Promise.all([
    registerMiddleware(server),
    registerRoutes(server)
])
.then(() => {
    server.start((err) => {
        if (err) {
            throw err;
        }
        console.log('RevCRM Server running at:', server.info.uri);
    });
})
.catch((err) => {
    console.error('Error occured initialising RevCRM', err);
});
