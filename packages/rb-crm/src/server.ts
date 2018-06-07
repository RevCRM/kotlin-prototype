import { RevCRMServer } from 'revcrm/lib/server';
import { MongoDBBackend } from 'rev-backend-mongodb';

(async () => {

    const mongo = new MongoDBBackend({
        url: 'mongodb://localhost:27017',
        dbName: 'rbcrm'
    });
    await mongo.connect();

    const server = new RevCRMServer();
    server.models.registerBackend('default', mongo);
    server.start();

})();
