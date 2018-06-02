
import { RevCRMServer } from 'revcrm/lib/server';
import { Account } from './models/AccountBackend';
import { populateAccounts } from './data/accounts';

export async function register(server: RevCRMServer) {
    server.models.register(Account);
    server.api.register(Account);

    await populateAccounts(server.models);
}
