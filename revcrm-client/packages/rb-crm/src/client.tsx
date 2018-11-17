import { RevCRMClient } from 'revcrm/lib/client';
import { registerModules } from './client_modules';

(async () => {
    const client = new RevCRMClient();
    await registerModules(client);
    client.start();
})();
