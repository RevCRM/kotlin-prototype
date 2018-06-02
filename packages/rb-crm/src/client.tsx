import { RevCRMClient } from 'revcrm/lib/client';
import { registerModules } from './client_modules';

const client = new RevCRMClient();
registerModules(client);
client.start();
