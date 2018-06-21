
import { RevCRMServer } from 'revcrm/lib/server';
import { Account } from './models/AccountBackend';
import { Address } from './models/Address';
import { AccountLink } from './models/AccountLink';

export async function register(server: RevCRMServer) {
    server.models.register(Address);
    server.models.register(Account);
    server.models.register(AccountLink);
    server.api.register(Address);
    server.api.register(Account);
    server.api.register(AccountLink);
}
