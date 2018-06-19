import { RevCRMServer } from 'revcrm/lib/server';
import { MongoDBBackend } from 'rev-backend-mongodb';

const MONGO_URL = process.env.MONGO_URL || 'mongodb://localhost:27017';

(async () => {

    console.log('Database URL:', MONGO_URL);
    const mongo = new MongoDBBackend({
        url: MONGO_URL,
        dbName: 'rbcrm'
    });
    await mongo.connect();

    const server = new RevCRMServer();
    server.models.registerBackend('default', mongo);
    server.start();

})();
