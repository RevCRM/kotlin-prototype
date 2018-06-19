import { RevCRMServer } from 'revcrm/lib/server';
import { MongoDBBackend } from 'rev-backend-mongodb';

const MONGO_URL = process.env.MONGO_URL || 'mongodb://localhost:27017';
const MONGO_DB_NAME = process.env.MONGO_DB_NAME || 'revcrm';
const PORT = Number(process.env.PORT) || 3000;

(async () => {

    console.log('Database URL:', MONGO_URL);
    console.log('Database Name:', MONGO_DB_NAME);
    const mongo = new MongoDBBackend({
        url: MONGO_URL,
        dbName: MONGO_DB_NAME
    });
    await mongo.connect();

    const server = new RevCRMServer({ port: PORT });
    server.models.registerBackend('default', mongo);
    server.start();

})();
